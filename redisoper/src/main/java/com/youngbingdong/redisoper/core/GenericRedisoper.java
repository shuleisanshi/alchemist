package com.youngbingdong.redisoper.core;

import com.youngbingdong.redisoper.core.metadata.EntityMetadata;
import com.youngbingdong.redisoper.serilize.Serializer;
import com.youngbingdong.redisoper.util.RedisoperUtil;
import com.youngbingdong.util.reflect.BeanUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static com.youngbingdong.util.Pager.of;
import static java.util.Collections.emptyList;
import static org.springframework.data.redis.core.ScanOptions.scanOptions;
import static org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive;
import static org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly;
import static org.springframework.util.Assert.notNull;

/**
 * @author ybd
 * @date 19-3-21
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class GenericRedisoper<T> {
	/* 所有 String KV 的集合 */
	private static final String KV_SET = ":KV:SET";
	/* 普通索引的 Key 的集合 */
	private static final String NIDX_SET = ":NIDX:SET";
	/* 自增Id Key */
	private static final String SEQ = ":SEQ";
	public static final int MAX_REDIS_PAGE = 500;
	public static final ScanOptions SCAN_OPTIONS = scanOptions().count(MAX_REDIS_PAGE).build();

	@Getter
	private EntityMetadata<T> entityMetadata;

	private final Class<T> entityClass;
	private final byte[] rawKvSetKey;
	private final byte[] rawNIdxSetKey;
	private final byte[] rawSeqKey;
	private final RedisSerializer<String> stringSerializer;
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	@Qualifier("tranRedisTemplate")
	private RedisTemplate<String, Object> tranRedisTemplate;
	@Autowired
	private Serializer valueSerializer;

	public GenericRedisoper(Class<T> entityClass, EntityMetadata<T> entityMetadata) {
		this.entityMetadata = entityMetadata;
		this.entityClass = entityClass;
		this.stringSerializer = RedisSerializer.string();
		this.rawKvSetKey = stringSerializer.serialize(entityClass.getSimpleName() + KV_SET);
		this.rawNIdxSetKey = stringSerializer.serialize(entityClass.getSimpleName() + NIDX_SET);
		this.rawSeqKey = stringSerializer.serialize(entityClass.getSimpleName() + SEQ);
	}

	public Long incrId(Supplier<Long> maxIdSupplier) {
		Long maxId = redisTemplate.execute((RedisCallback<Long>) connection -> connection.incr(rawSeqKey));
		if (maxId == null || maxId <= 1) {
			maxId = maxIdSupplier.get();
			resetIncrId(maxId);
		}
		return maxId;
	}

	public void resetIncrId(Long id) {
		redisTemplate.execute((RedisCallback<Void>) connection -> {
			connection.set(rawSeqKey, stringSerializer.serialize(id.toString()));
			return null;
		});
	}

	@SuppressWarnings("unchecked")
	public T getByKey(Supplier<T> dbLoader, Object... value) {
		String primaryKey = entityMetadata.genRedisPrimaryKey(value);
		T o = (T) RedisTransactionResourceHolder.get(primaryKey);
		if (o != null) {
			return o;
		}
		return redisTemplate.execute((RedisCallback<T>) connection -> {
			byte[] rawValue = connection.get(rawKey(primaryKey));
			T entity = deserializeValue(rawValue);
			return loadFromDbAndSet2RedisIfNull(dbLoader, entity);
		});
	}

	public T getByUniqueIndex(Supplier<T> dbLoader, String uniqueIndex, Object... values) {
		checkContainIndex();
		return redisTemplate.execute((RedisCallback<T>) connection -> {
			String redisUniqueIndexKey = entityMetadata.genRedisUniqueIndexKey(uniqueIndex, values);
			T entity = null;
			byte[] rawPrimaryKey = connection.get(rawKey(redisUniqueIndexKey));
			if (rawPrimaryKey != null) {
				byte[] rawValue = connection.get(rawPrimaryKey);
				entity = deserializeValue(rawValue);
			}
			return loadFromDbAndSet2RedisIfNull(dbLoader, entity);
		});
	}

	private void checkContainIndex() {
		Assert.isTrue(entityMetadata.isContainIndex(), entityMetadata.getEntityName() + " do not contain index");
	}

	public List<T> getByNormalIndex(Supplier<List<T>> dbLoader, String normalIndex, Object... values) {
		checkContainIndex();
		return redisTemplate.execute((RedisCallback<List<T>>) connection -> {
			String normalRedisIndexKey = entityMetadata.genRedisNormalRedisIndexKey(normalIndex, values);
			byte[] rawNormalRedisIndexKey = rawKey(normalRedisIndexKey);
			Set<byte[]> rawPrimaryKeySet = connection.sMembers(rawNormalRedisIndexKey);
			List<T> entities;
			if (rawPrimaryKeySet != null && !rawPrimaryKeySet.isEmpty()) {
				List<byte[]> rawValues = connection.mGet(rawPrimaryKeySet.toArray(new byte[rawPrimaryKeySet.size()][]));
				if (rawValues != null && rawValues.size() > 0) {
					boolean dirty = false;
					entities = new ArrayList<>(rawValues.size());
					for (byte[] rawValue : rawValues) {
						if (rawValue == null) {
							dirty = true;
							break;
						}
						entities.add(deserializeValue(rawValue));
					}
					if (!dirty) {
						return entities;
					}
				}
			}
			connection.unlink(rawNormalRedisIndexKey);
			connection.sRem(rawNIdxSetKey, rawNormalRedisIndexKey);
			entities = dbLoader.get();
			if (entities != null && entities.size() > 0) {
				batchSet2Redis(entities, false);
			}
			return entities == null ? emptyList() : entities;
		});
	}

	public void set2Redis(T entity) {
		set2Redis(entity, true);
	}

	public void set2Redis(T entity, boolean enableTransaction) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			RedisoperUtil.doInPipeline(connection, conn -> {
				byte[] rawPrimaryKey = rawPrimaryKey(entity);
				List<byte[]> allKeys = new ArrayList<>(16);
				conn.set(rawPrimaryKey, rawValue(entity));
				allKeys.add(rawPrimaryKey);
				if (entityMetadata.isContainIndex()) {
					entityMetadata.foreachIndex(entityIndex -> {
						byte[] indexRawKey = rawKey(entityIndex.genRedisIndexKeyByEntity(entity));
						if (entityIndex.unique()) {
							conn.set(indexRawKey, rawPrimaryKey);
							allKeys.add(indexRawKey);
						} else {
							conn.sAdd(indexRawKey, rawPrimaryKey);
							conn.sAdd(rawNIdxSetKey, indexRawKey);
						}
					});
				}
				conn.sAdd(rawKvSetKey, allKeys.toArray(new byte[allKeys.size()][]));
			});
			return null;
		});
	}

	public void batchSet2Redis(List<T> entities) {
		batchSet2Redis(entities, true);
	}

	public void batchSet2Redis(List<T> entities, boolean enableTransaction) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			RedisoperUtil.doInPipeline(connection, conn -> of(entities, MAX_REDIS_PAGE).doPage(e -> batchSet2RedisInner(conn, e)));
			return null;
		});
	}

	private void batchSet2RedisInner(RedisConnection conn, List<T> innerEntities) {
		int initialCapacity = innerEntities.size() * 3;
		Map<byte[], byte[]> mSetMapHolder = new HashMap<>(initialCapacity);
		List<byte[]> allKvs = new ArrayList<>(initialCapacity);
		for (T entity : innerEntities) {
			byte[] rawPrimaryKey = rawPrimaryKey(entity);
			mSetMapHolder.put(rawPrimaryKey, rawValue(entity));
			if (entityMetadata.isContainIndex()) {
				entityMetadata.foreachIndex(entityIndex -> {
					byte[] indexRawKey = rawKey(entityIndex.genRedisIndexKeyByEntity(entity));
					if (entityIndex.unique()) {
						mSetMapHolder.put(indexRawKey, rawPrimaryKey);
						allKvs.add(indexRawKey);
					} else {
						conn.sAdd(indexRawKey, rawPrimaryKey);
						conn.sAdd(rawNIdxSetKey, indexRawKey);
					}
				});
			}
		}
		allKvs.addAll(mSetMapHolder.keySet());
		conn.mSet(mSetMapHolder);
		conn.sAdd(rawKvSetKey, allKvs.toArray(new byte[allKvs.size()][]));
	}

	public T updateInRedis(Supplier<T> oldEntityGetter, T newEntity) {
		return updateInRedis(oldEntityGetter, newEntity, true);
	}

	public T updateInRedis(Supplier<T> oldEntityGetter, T newEntity, boolean enableTransaction) {
		return getRedisTemplate(enableTransaction).execute((RedisCallback<T>) connection -> {
			String primaryKey = entityMetadata.genRedisPrimaryKeyByEntity(newEntity);
			byte[] rawPrimaryKey = rawKey(primaryKey);
			T oldEntity = getByKey(oldEntityGetter, entityMetadata.getPrimaryValue(newEntity));
			notNull(oldEntity, "Old entity could not be null for update");
			List<byte[]> allKeysToBeAdd = new ArrayList<>(16);
			RedisoperUtil.doInPipeline(connection, conn -> {
				if (entityMetadata.isContainIndex()) {
					entityMetadata.foreachIndex(entityIndex -> {
						if (entityIndex.indexChanged(oldEntity, newEntity)) {
							byte[] rawOldIndexKey = rawKey(entityIndex.genRedisIndexKeyByEntity(oldEntity));
							byte[] rawNewIndexKey = rawKey(entityIndex.genRedisIndexKeyByEntity(newEntity));
							if (entityIndex.unique()) {
								conn.del(rawOldIndexKey);
								conn.sRem(rawKvSetKey, rawOldIndexKey);
								conn.set(rawNewIndexKey, rawPrimaryKey);
								allKeysToBeAdd.add(rawNewIndexKey);
							} else {
								conn.sRem(rawOldIndexKey, rawPrimaryKey);
								conn.sRem(rawNIdxSetKey, rawOldIndexKey);
								conn.sAdd(rawNewIndexKey, rawPrimaryKey);
								conn.sAdd(rawNIdxSetKey, rawNewIndexKey);
							}
						}
					});
				}
				BeanUtil.copyPropertiesIgnoreNull(newEntity, oldEntity);
				conn.set(rawPrimaryKey, rawValue(oldEntity));
				allKeysToBeAdd.add(rawPrimaryKey);
				conn.sAdd(rawKvSetKey, allKeysToBeAdd.toArray(new byte[allKeysToBeAdd.size()][]));
				if (isActualTransactionActive() && !isCurrentTransactionReadOnly()) {
					RedisTransactionResourceHolder.bindResource(primaryKey, oldEntity);
				}
			});
			return oldEntity;
		});
	}

	public void deleteInRedis(T entity) {
		deleteInRedis(entity, true);
	}

	public void deleteInRedis(T entity, boolean enableTransaction) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			RedisoperUtil.doInPipeline(connection, conn -> {
				byte[] rawPrimaryKey = rawPrimaryKey(entity);
				conn.del(rawPrimaryKey);
				List<byte[]> rawKeysToBeDelete = new ArrayList<>(16);
				rawKeysToBeDelete.add(rawPrimaryKey);
				if (entityMetadata.isContainIndex()) {
					entityMetadata.foreachIndex(entityIndex -> {
							byte[] rawIndexKey = rawKey(entityIndex.genRedisIndexKeyByEntity(entity));
							if (entityIndex.unique()) {
								conn.del(rawIndexKey);
								rawKeysToBeDelete.add(rawIndexKey);
							} else {
								conn.sRem(rawIndexKey, rawPrimaryKey);
								conn.sRem(rawNIdxSetKey, rawIndexKey);
							}
					});
				}
				conn.sRem(rawKvSetKey, rawKeysToBeDelete.toArray(new byte[rawKeysToBeDelete.size()][]));
			});
			return null;
		});
	}

	public void batchDeleteInRedis(List<T> entities, boolean enableTransaction) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			RedisoperUtil.doInPipeline(connection, conn -> of(entities, MAX_REDIS_PAGE).doPage(e -> batchDeleteInRedisInner(conn, e)));
			return null;
		});
	}

	private void batchDeleteInRedisInner(RedisConnection conn, List<T> entities) {
		List<byte[]> rawPrimaryKeysToBeDelete = new ArrayList<>(entities.size() * 2);
		List<byte[]> rawKeysToBeSRem = new ArrayList<>(entities.size() * 2);
		for (T entity : entities) {
			byte[]  rawPrimaryKey = rawPrimaryKey(entity);
			rawPrimaryKeysToBeDelete.add(rawPrimaryKey);
			rawKeysToBeSRem.add(rawPrimaryKey);
			if (entityMetadata.isContainIndex()) {
				entityMetadata.foreachIndex(entityIndex -> {
					byte[] rawIndexKey = rawKey(entityIndex.genRedisIndexKeyByEntity(entity));
					if (entityIndex.unique()) {
						rawPrimaryKeysToBeDelete.add(rawIndexKey);
						rawKeysToBeSRem.add(rawIndexKey);
					} else {
						conn.sRem(rawIndexKey, rawPrimaryKey);
						conn.sRem(rawNIdxSetKey, rawIndexKey);
					}
				});
			}
		}
		conn.del(rawPrimaryKeysToBeDelete.toArray(new byte[rawPrimaryKeysToBeDelete.size()][]));
		conn.sRem(rawKvSetKey, rawKeysToBeSRem.toArray(new byte[rawKeysToBeSRem.size()][]));
	}

	public void batchDeleteInRedis(List<T> entities) {
		batchDeleteInRedis(entities, true);
	}

	public void deleteAllInRedis() {
		redisTemplate.execute((RedisCallback<Void>) connection -> {
			int count = 0;
			int maxCount = MAX_REDIS_PAGE;
			byte[][] rawKeyArray = new byte[maxCount][];
			Cursor<byte[]> cursor = connection.sScan(rawKvSetKey, SCAN_OPTIONS);
			while (cursor.hasNext()) {
				rawKeyArray[count++] = cursor.next();
				if (count == maxCount) {
					connection.sRem(rawKvSetKey, rawKeyArray);
					connection.del(rawKeyArray);
					count = 0;
				}
			}
			if (count != 0) {
				byte[][] lastPage = new byte[count][];
				System.arraycopy(rawKeyArray, 0, lastPage, 0, count);
				connection.sRem(rawKvSetKey, lastPage);
				connection.del(lastPage);
				count = 0;
			}

			cursor = connection.sScan(rawNIdxSetKey, SCAN_OPTIONS);
			while (cursor.hasNext()) {
				rawKeyArray[count++] = cursor.next();
				if (count == maxCount) {
					connection.sRem(rawNIdxSetKey, rawKeyArray);
					connection.unlink(rawKeyArray);
					count = 0;
				}
			}
			if (count != 0) {
				byte[][] lastPage = new byte[count][];
				System.arraycopy(rawKeyArray, 0, lastPage, 0, count);
				connection.sRem(rawNIdxSetKey, lastPage);
				connection.del(lastPage);
			}
			connection.del(rawSeqKey);
			connection.unlink(rawKvSetKey, rawNIdxSetKey);
			for (int i = 0; i < maxCount; i++) {
				rawKeyArray[i] = null;
			}
			return null;
		});
	}

	private T loadFromDbAndSet2RedisIfNull(Supplier<T> dbLoader, T entity) {
		if (entity == null) {
			entity = dbLoader.get();
			if (entity != null) {
				set2Redis(entity, false);
			}
		}
		return entity;
	}

	public RedisTemplate<String, Object> getRedisTemplate(boolean enableTransaction) {
		return enableTransaction ? tranRedisTemplate : redisTemplate;
	}

	/* ################################################################ Convert ################################################################ */

	private byte[] rawPrimaryKey(T entity) {
		return rawKey(entityMetadata.genRedisPrimaryKeyByEntity(entity));
	}

	private byte[] rawKey(String key) {
		return stringSerializer.serialize(key);
	}

	private byte[] rawValue(@NonNull T value) {
		return valueSerializer.serialize(value);
	}

	private T deserializeValue(byte[] entityBytes) {
		return entityBytes == null ? null : valueSerializer.deserialize(entityBytes, entityClass);
	}

}
