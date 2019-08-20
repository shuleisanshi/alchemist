package com.youngbingdong.redisoper.core;

/**
 * @author ybd
 * @date 19-3-24
 * @contact yangbingdong1994@gmail.com
 */
public interface RedisoperAware<T> {

	void setRedisoper(GenericRedisoper<T> redisoper);
}
