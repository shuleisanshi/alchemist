package com.youngbingdong.redisoper.util;

import org.springframework.data.redis.connection.RedisConnection;

/**
 * @author ybd
 * @date 19-4-28
 * @contact yangbingdong1994@gmail.com
 */
public interface PipelineCallback {

	void doPipelineCallback(RedisConnection connection);
}
