package com.youngbingdong.redisoper.core.metadata.index;

import java.io.Serializable;

/**
 * @author ybd
 * @date 19-3-23
 * @contact yangbingdong1994@gmail.com
 */
public interface IndexWriter<T> extends Serializable {
	void write(T t, Object... values);

}
