package com.youngbingdong.redisoper.serilize;

/**
 * @author ybd
 * @date 18-9-28
 * @contact yangbingdong1994@gmail.com
 */
public interface Serializer {

	<T> byte[] serialize(T obj);

	<T> T deserialize(byte[] data, Class<T> clazz);
}
