package com.youngbingdong.util.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ybd
 * @date 19-3-25
 * @contact yangbingdong1994@gmail.com
 */
public class TypeUtil {

	public static Class<?> getClassFromGenericTypeInterface(Object source, Class<?> interfaceClass) {
		Class<?> genericClass = null;
		Class<?> beanClass = source.getClass();
		Type[] genericInterfaces = beanClass.getGenericInterfaces();
		for (Type genericInterface : genericInterfaces) {
			if (genericInterface instanceof ParameterizedType && ((ParameterizedType)genericInterface).getRawType() == interfaceClass) {
				genericClass = (Class<?>) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
			}
		}
		if (genericClass == null) {
			Type genericSuperclass = beanClass.getGenericSuperclass();
			if (genericSuperclass instanceof ParameterizedType && ((ParameterizedType)genericSuperclass).getRawType() == interfaceClass) {
				genericClass = (Class<?>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
			}
		}
		return genericClass;
	}
}
