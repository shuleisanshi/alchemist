package com.youngbingdong.redisoper.core.metadata.index;

import com.youngbingdong.redisoper.core.metadata.EntityMetadata;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import static com.youngbingdong.redisoper.core.metadata.index.EntityIndex.getIdxKeyPrefix;

/**
 * @author ybd
 * @date 19-3-22
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class EntitySingleIndex<T> implements EntityIndex<T> {

	private boolean primary;

	private String entityName;

	private boolean unique;

	private EntityIndexEntry indexEntry;

	@Override
	public boolean unique() {
		return unique;
	}

	@Override
	public String genRedisIndexKey(Object... values) {
		Assert.isTrue(values.length == 1, "Single index length must be equal to 1");
		return getRedisIndexKeyInner(values[0]);
	}

	@Override
	public String genRedisIndexKeyByEntity(T entity) {
		Object value = indexEntry.read(entity);
		return getRedisIndexKeyInner(value);
	}

	@Override
	public boolean indexChanged(T oldEntity, T newEntity) {
		return !indexEntry.read(oldEntity).equals(indexEntry.read(newEntity));
	}

	@Override
	public Object getIndexValue(T entity) {
		return indexEntry.read(entity);
	}

	@Override
	public void injectIndexValue(T entity, Object... values) {
		indexEntry.write(entity, values);
	}

	private String getRedisIndexKeyInner(Object value) {
		Assert.notNull(value, "Index value could not be null");
		if (primary) {
			return entityName + EntityMetadata.SEMICOLON + value;
		}
		return entityName + getIdxKeyPrefix(unique) + indexEntry.getIndexFieldCap() + EntityMetadata.SEMICOLON + value;
	}
}
