package com.youngboss.ccredisoper.core.metadata.index;

import static com.youngboss.ccredisoper.core.metadata.EntityMetadata.SEMICOLON;

/**
 * @author ybd
 * @date 19-3-22
 * @contact yangbingdong1994@gmail.com
 */
public interface EntityIndex<T> {

	String IDX_KEY_PREFIX = SEMICOLON + "NIdx" + SEMICOLON;
	String UNIQUE_IDX_KEY_PREFIX = SEMICOLON + "UIdx" + SEMICOLON;

	static String getIdxKeyPrefix(boolean unique){
		return unique ? UNIQUE_IDX_KEY_PREFIX : IDX_KEY_PREFIX;
	}

	boolean unique();

	String genRedisIndexKey(Object... value);

	String genRedisIndexKeyByEntity(T entity);

	boolean indexChanged(T oldEntity, T newEntity);

	Object getIndexValue(T entity);

	void injectIndexValue(T entity, Object... values);

}
