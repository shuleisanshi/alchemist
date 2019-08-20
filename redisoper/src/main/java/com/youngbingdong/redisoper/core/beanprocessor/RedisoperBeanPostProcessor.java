package com.youngbingdong.redisoper.core.beanprocessor;

import com.youngbingdong.redisoper.core.RedisoperAware;

import static com.youngbingdong.util.reflect.TypeUtil.getClassFromGenericTypeInterface;

/**
 * @author ybd
 * @date 19-3-24
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperBeanPostProcessor extends AbstractBeanPostProcessor {

	@Override
	Class resolveEntityClass(Object bean) {
		return getClassFromGenericTypeInterface(bean, RedisoperAware.class);
	}

}
