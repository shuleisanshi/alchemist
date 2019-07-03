package com.youngbingdong.ccredisoper.core.metadata;

import com.youngbingdong.ccredisoper.core.metadata.index.EntityIndex;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author ybd
 * @date 19-3-21
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class EntityMetadata<T> {
	public static final String SEMICOLON = ":";

	private Class<T> entityClass;
	private String entityName;
	private EntityIndex<T> entityPrimaryKey;

	private boolean containIndex = false;
	private Map<String, EntityIndex<T>> indexMap;
	private Collection<EntityIndex<T>> entityIndexes;

	public String genRedisPrimaryKey(Object...values) {
		return entityPrimaryKey.genRedisIndexKey(values);
	}

	public String genRedisPrimaryKeyByEntity(T entity) {
		return entityPrimaryKey.genRedisIndexKeyByEntity(entity);
	}

	public String genRedisUniqueIndexKey(String uniqueIndex, Object... values) {
		EntityIndex<T> entityIndex = indexMap.get(uniqueIndex);
		if (entityIndex == null || !entityIndex.unique()) {
			throw new IllegalArgumentException("Invalid unique index key: " + uniqueIndex);
		}
		return entityIndex.genRedisIndexKey(values);
	}

	public String genRedisNormalRedisIndexKey(String normalIndex, Object... values) {
		EntityIndex<T> entityIndex = indexMap.get(normalIndex);
		if (entityIndex == null || entityIndex.unique()) {
			throw new IllegalArgumentException("Invalid normal index key: " + normalIndex);
		}
		return entityIndex.genRedisIndexKey(values);
	}

	public void foreachIndex(Consumer<? super EntityIndex<T>> action) {
		this.entityIndexes.forEach(action);
	}

	public EntityMetadata(Class<T> entityClass) {
		this.entityClass = entityClass;
		entityName = StringUtils.capitalize(entityClass.getSimpleName());
	}

	public void setIndexMap(Map<String, EntityIndex<T>> indexMap) {
		this.indexMap = indexMap;
		this.entityIndexes = indexMap.values();
	}

	public Object getPrimaryValue(T entity) {
		return entityPrimaryKey.getIndexValue(entity);
	}

	public void injectPrimaryValue(T entity, Object value) {
		entityPrimaryKey.injectIndexValue(entity, value);
	}
}
