package com.youngbingdong.ccredisoper.core.beanprocessor;

import java.lang.reflect.ParameterizedType;

/**
 * @author ybd
 * @date 19-3-24
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperMpBeanPostProcessor extends AbstractBeanPostProcessor {

	@Override
	Class resolveEntityClass(Object bean) {
		ParameterizedType type = (ParameterizedType) bean.getClass().getGenericSuperclass();
		return (Class<?>) type.getActualTypeArguments()[1];
	}
}
