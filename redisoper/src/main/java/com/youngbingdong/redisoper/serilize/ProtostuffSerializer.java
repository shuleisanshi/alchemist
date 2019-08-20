package com.youngbingdong.redisoper.serilize;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * @author ybd
 * @date 19-3-20
 * @contact yangbingdong1994@gmail.com
 */
public class ProtostuffSerializer implements Serializer {
	private static Objenesis objenesis = new ObjenesisStd(true);

	@Override
	@SuppressWarnings("unchecked")
	public <T> byte[] serialize(T obj) {
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Class<T> cls = (Class<T>) obj.getClass();
			return ProtostuffIOUtil.toByteArray(obj, RuntimeSchema.getSchema(cls), buffer);
		} finally {
			buffer.clear();
		}
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> cls) {
		T message = objenesis.newInstance(cls);
		ProtostuffIOUtil.mergeFrom(data, message, RuntimeSchema.getSchema(cls));
		return message;
	}

}
