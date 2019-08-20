package com.youngbingdong.redisoper.core;

import org.springframework.core.NamedThreadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ybd
 * @date 19-5-14
 * @contact yangbingdong1994@gmail.com
 */
public final class RedisTransactionResourceHolder {

	private static final ThreadLocal<Map<String, Object>> resources = new NamedThreadLocal<>("Redis resources");

	public static void bindResource(String key, Object value) {
		Map<String, Object> map = resources.get();
		if (map == null) {
			map = new HashMap<>();
			resources.set(map);
		}
		map.put(key, value);
	}

	public static Object get(String key) {
		Map<String, Object> map = resources.get();
		return map == null ? null : map.get(key);
	}

	public static void clear() {
		resources.remove();
	}

}
