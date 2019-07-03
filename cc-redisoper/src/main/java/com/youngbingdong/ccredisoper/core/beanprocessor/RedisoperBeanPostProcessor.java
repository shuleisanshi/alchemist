package com.youngbingdong.ccredisoper.core.beanprocessor;

import com.youngbingdong.ccredisoper.core.RedisoperAware;

import static com.youngbingdong.ccutil.reflect.TypeUtil.getClassFromGenericTypeInterface;

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
