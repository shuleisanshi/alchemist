package com.youngboss.ccredisoper.util;

import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ybd
 * @date 19-4-28
 * @contact yangbingdong1994@gmail.com
 */
public class RedisoperUtil {

	private static Method getConnectionMethod;

	static {
		try {
			getConnectionMethod = LettuceConnection.class.getDeclaredMethod("getConnection");
			getConnectionMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static RedisClusterCommands<byte[], byte[]> reflectGetRedisClusterCommands(RedisConnection connection) {
		try {
			return (RedisClusterCommands<byte[], byte[]>) getConnectionMethod.invoke(connection);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void doInPipeline(RedisConnection connection, PipelineCallback pipelineCallback) {
		doInPipelineInner(connection, pipelineCallback);
	}

	public static void doInPipelineInner(RedisConnection connection, PipelineCallback pipelineCallback) {
		connection.openPipeline();
		boolean pipelinedClosed = false;
		try {
			pipelineCallback.doPipelineCallback(connection);
			connection.closePipeline();
			pipelinedClosed = true;
		} finally {
			if (!pipelinedClosed) {
				connection.closePipeline();
			}
		}
	}

	public static void doInPipelineInnerWithoutClose(RedisConnection connection, PipelineCallback pipelineCallback) {
		connection.openPipeline();
		pipelineCallback.doPipelineCallback(connection);
	}

	public static void doNotThingPipeline(RedisConnection connection, PipelineCallback pipelineCallback) {
		pipelineCallback.doPipelineCallback(connection);
	}
}
