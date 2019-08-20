package com.youngbingdong.redisoper.core.beanprocessor;

import com.youngbingdong.redisoper.core.RedisoperAware;
import com.youngbingdong.redisoper.core.metadata.EntityMetadata;
import com.youngbingdong.redisoper.core.metadata.annotation.RedisIndex;
import com.youngbingdong.redisoper.core.metadata.annotation.RedisPrimaryKey;
import com.youngbingdong.redisoper.core.metadata.index.EntityIndex;
import com.youngbingdong.redisoper.core.metadata.index.EntityIndexEntry;
import com.youngbingdong.redisoper.core.metadata.index.EntitySingleIndex;
import com.youngbingdong.redisoper.core.metadata.index.EntityUnionIndex;
import com.youngbingdong.redisoper.core.metadata.index.IndexReader;
import com.youngbingdong.redisoper.core.metadata.index.IndexWriter;
import com.youngbingdong.redisoper.core.GenericRedisoper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ybd
 * @date 19-3-29
 * @contact yangbingdong1994@gmail.com
 */
public abstract class AbstractBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof RedisoperAware){
			Class entityClass = resolveEntityClass(bean);
			Assert.notNull(entityClass, "解析错误");
			injectRedisoper((RedisoperAware) bean, entityClass);
		}
		return bean;
	}

	abstract Class resolveEntityClass(Object bean);

	@SuppressWarnings({"unchecked"})
	private void injectRedisoper(RedisoperAware bean, Class entityClass) {
		List<EntityIndexEntry> primaryKeyList = new ArrayList<>(16);
		Map<String, List<EntityIndexEntry>> indexMap = new HashMap<>(16);

		for (Field declaredField : entityClass.getDeclaredFields()) {
			String fieldName = declaredField.getName();
			String fieldNameCap = StringUtils.capitalize(fieldName);
			if (declaredField.isAnnotationPresent(RedisPrimaryKey.class)) {
				RedisPrimaryKey redisPrimaryKey = declaredField.getAnnotation(RedisPrimaryKey.class);
				EntityIndexEntry primaryKey = new EntityIndexEntry();
				primaryKey.setIndexField(fieldName)
						  .setIndexFieldCap(fieldNameCap)
						  .setOrder(redisPrimaryKey.order());
				handlerReaderAndWriter(primaryKey, entityClass, fieldName);
				primaryKeyList.add(primaryKey);
			}
			if (declaredField.isAnnotationPresent(RedisIndex.class)) {
				RedisIndex redisIndex = declaredField.getAnnotation(RedisIndex.class);
				EntityIndexEntry indexEntry = new EntityIndexEntry();
				indexEntry.setUnique(redisIndex.unique())
						  .setOrder(redisIndex.order())
						  .setIndexField(fieldName)
						  .setIndexFieldCap(fieldNameCap);
				handlerReaderAndWriter(indexEntry, entityClass, fieldName);
				indexMap.compute(redisIndex.name(), (s, entityIndexEntries) -> {
					if (entityIndexEntries == null || entityIndexEntries.size() == 0) {
						List<EntityIndexEntry> indexEntries = new ArrayList<>(16);
						indexEntries.add(indexEntry);
						return indexEntries;
					}
					entityIndexEntries.add(indexEntry);
					return entityIndexEntries;
				});
			}
		}

		EntityMetadata metadata = new EntityMetadata<>(entityClass);
		handlePrimaryKey(metadata, primaryKeyList);
		handlerIndex(metadata, indexMap);

		GenericRedisoper genericRedisoper = new GenericRedisoper<>(entityClass, metadata);
		autowireCapableBeanFactory.autowireBean(genericRedisoper);
		bean.setRedisoper(genericRedisoper);
	}

	@SuppressWarnings({"unchecked"})
	private void handlerIndex(EntityMetadata metadata, Map<String, List<EntityIndexEntry>> indexMap) {
		if (indexMap != null && indexMap.size() > 0) {
			Map<String, EntityIndex> entityIndexMap = new HashMap<>(indexMap.size());
			metadata.setContainIndex(true);
			indexMap.forEach((k, entityIndexEntries) -> {
				if (entityIndexEntries.size() == 1) {
					EntityIndexEntry indexEntry = entityIndexEntries.get(0);
					EntitySingleIndex singleIndex = new EntitySingleIndex<>();
					singleIndex.setEntityName(metadata.getEntityName())
							   .setPrimary(false)
							   .setUnique(indexEntry.isUnique())
							   .setIndexEntry(indexEntry);
					entityIndexMap.put(k, singleIndex);
				} else {
					entityIndexEntries.sort(Comparator.comparing(EntityIndexEntry::getOrder));
					EntityUnionIndex unionIndex = new EntityUnionIndex<>();
					unionIndex.setEntityName(metadata.getEntityName())
							  .setPrimary(false)
							  .setUnique(entityIndexEntries.get(0).isUnique())
							  .setSize(entityIndexEntries.size())
							  .setIndexEntries(entityIndexEntries.toArray(new EntityIndexEntry[0]));
					entityIndexMap.put(k, unionIndex);
				}

			});
			metadata.setIndexMap(entityIndexMap);
		}
	}

	@SuppressWarnings({"unchecked"})
	private void handlePrimaryKey(EntityMetadata metadata, List<EntityIndexEntry> primaryKeyEntries) {
		int size = primaryKeyEntries.size();
		Assert.isTrue(size > 0, "Primary key length must greater then 0");
		if (size == 1) {
			EntitySingleIndex singlePrimaryKey = new EntitySingleIndex<>();
			singlePrimaryKey.setEntityName(metadata.getEntityName())
							.setPrimary(true)
							.setUnique(true)
							.setIndexEntry(primaryKeyEntries.get(0));
			metadata.setEntityPrimaryKey(singlePrimaryKey);

		} else {
			primaryKeyEntries.sort(Comparator.comparing(EntityIndexEntry::getOrder));
			EntityUnionIndex entityUnionPrimaryKey = new EntityUnionIndex<>();
			entityUnionPrimaryKey.setEntityName(metadata.getEntityName())
								 .setSize(size)
								 .setPrimary(true)
								 .setUnique(true)
								 .setIndexEntries(primaryKeyEntries.toArray(new EntityIndexEntry[0]));
			metadata.setEntityPrimaryKey(entityUnionPrimaryKey);
		}
	}

	@SuppressWarnings({"unchecked"})
	private void handlerReaderAndWriter(EntityIndexEntry primaryKey, Class entityClass, String fieldName) {
		primaryKey.setReader(buildIndexReader(entityClass, fieldName))
				  .setWriter(buildIndexWriter(entityClass, fieldName));
	}

	@SuppressWarnings("ConstantConditions")
	private IndexReader buildIndexReader(Class entityClass, String name) {
		Method readMethod = BeanUtils.getPropertyDescriptor(entityClass, name).getReadMethod();
		return (IndexReader<Object>) o -> {
			try {
				return readMethod.invoke(o);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		};
	}

	@SuppressWarnings("ConstantConditions")
	private IndexWriter buildIndexWriter(Class entityClass, String fieldName) {
		Method writerMethod = BeanUtils.getPropertyDescriptor(entityClass, fieldName).getWriteMethod();
		return (IndexWriter) (o, values) -> {
			try {
				writerMethod.invoke(o, values);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		};
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
	}

}
