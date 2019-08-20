package com.youngbingdong.redisoper.core.metadata.index;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ybd
 * @date 19-3-23
 * @contact yangbingdong1994@gmail.com
 */
public interface IndexReader<T> extends Serializable {
	Object read(T t);

	default String toFieldName() {
		try {
			Method method = getClass().getDeclaredMethod("writeReplace");
			method.setAccessible(Boolean.TRUE);
			SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
			String getter = serializedLambda.getImplMethodName();
			return getter.replace("get", "");
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
