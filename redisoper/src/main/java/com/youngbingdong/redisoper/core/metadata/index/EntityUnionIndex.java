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
public class EntityUnionIndex<T> implements EntityIndex<T> {

	private boolean primary;

	private boolean unique;

	private String entityName;

	private int size;

	private EntityIndexEntry<T>[] indexEntries;


	@Override
	public boolean unique() {
		return unique;
	}

	@Override
	public String genRedisIndexKey(Object... values) {
		Assert.isTrue(size == values.length, "Union index length not match");
		StringBuilder builder = resolveStringBuilder();
		Object value;
		if (primary) {
			for (int i = 0; i < size; i++) {
				value = values[i];
				Assert.notNull(value, "Index value could not be null");
				builder.append(EntityMetadata.SEMICOLON)
					   .append(value);
			}
		} else {
			for (int i = 0; i < size; i++) {
				value = values[i];
				Assert.notNull(value, "Index value could not be null");
				builder.append(EntityMetadata.SEMICOLON)
					   .append(indexEntries[i].getIndexFieldCap())
					   .append(EntityMetadata.SEMICOLON)
					   .append(value);
			}
		}
		return builder.toString();
	}

	@Override
	public String genRedisIndexKeyByEntity(T entity) {
		StringBuilder builder = resolveStringBuilder();
		Object value;
		if (primary) {
			for (EntityIndexEntry<T> indexEntry : indexEntries) {
				value = indexEntry.read(entity);
				Assert.notNull(value, "Index value could not be null");
				builder.append(EntityMetadata.SEMICOLON)
					   .append(value);
			}
		} else {
			for (EntityIndexEntry<T> indexEntry : indexEntries) {
				value = indexEntry.read(entity);
				Assert.notNull(value, "Index value could not be null");
				builder.append(EntityMetadata.SEMICOLON)
					   .append(indexEntry.getIndexFieldCap())
					   .append(EntityMetadata.SEMICOLON)
					   .append(value);
			}
		}

		return builder.toString();
	}

	@Override
	public boolean indexChanged(T oldEntity, T newEntity) {
		boolean changed = false;
		for (EntityIndexEntry<T> indexEntry : indexEntries) {
			if (!indexEntry.read(oldEntity).equals(indexEntry.read(newEntity))) {
				changed = true;
				break;
			}
		}
		return changed;
	}

	@Override
	public Object getIndexValue(T entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void injectIndexValue(T entity, Object... values) {
		throw new UnsupportedOperationException();
	}

	private StringBuilder resolveStringBuilder() {
		return primary ? new StringBuilder(entityName) : new StringBuilder(entityName).append(getIdxKeyPrefix(unique));
	}

}
