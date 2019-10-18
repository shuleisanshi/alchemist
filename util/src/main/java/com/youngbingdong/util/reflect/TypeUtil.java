package com.youngbingdong.util.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ybd
 * @date 19-3-25
 * @contact yangbingdong1994@gmail.com
 */
public class TypeUtil {

	public static Class<?> getClassFromGenericTypeInterface(Class<?> beanClass, Class<?> interfaceClass) {
		Type[] genericInterfaces = beanClass.getGenericInterfaces();
		for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof Class && interfaceClass.isAssignableFrom((Class<?>) genericInterface)) {
                return getClassFromGenericTypeInterface((Class<?>) genericInterface, interfaceClass);
            }
            if (genericInterface instanceof ParameterizedType && interfaceClass.isAssignableFrom((Class<?>) ((ParameterizedType) genericInterface).getRawType())) {
                return (Class<?>) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
            }
		}
		return null;
	}

    public static Class<?> getClassFromGenericSupperClass(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        }
        return getClassFromGenericSupperClass((Class<?>) genericSuperclass);
    }
}
