package com.youngboss.ccredisoper.core.metadata.index;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import static com.youngboss.ccredisoper.core.metadata.EntityMetadata.SEMICOLON;
import static com.youngboss.ccredisoper.core.metadata.index.EntityIndex.getIdxKeyPrefix;

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
				builder.append(SEMICOLON)
					   .append(value);
			}
		} else {
			for (int i = 0; i < size; i++) {
				value = values[i];
				Assert.notNull(value, "Index value could not be null");
				builder.append(SEMICOLON)
					   .append(indexEntries[i].getIndexFieldCap())
					   .append(SEMICOLON)
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
				value = indexEntry.getReader().read(entity);
				Assert.notNull(value, "Index value could not be null");
				builder.append(SEMICOLON)
					   .append(value);
			}
		} else {
			for (EntityIndexEntry<T> indexEntry : indexEntries) {
				value = indexEntry.getReader().read(entity);
				Assert.notNull(value, "Index value could not be null");
				builder.append(SEMICOLON)
					   .append(indexEntry.getIndexFieldCap())
					   .append(SEMICOLON)
					   .append(value);
			}
		}

		return builder.toString();
	}

	@Override
	public boolean indexChanged(T oldEntity, T newEntity) {
		boolean changed = false;
		IndexReader<T> reader;
		for (EntityIndexEntry<T> indexEntry : indexEntries) {
			reader = indexEntry.getReader();
			if (!reader.read(oldEntity).equals(reader.read(newEntity))) {
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
