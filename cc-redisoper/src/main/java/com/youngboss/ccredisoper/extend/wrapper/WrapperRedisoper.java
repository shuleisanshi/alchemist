package com.youngboss.ccredisoper.extend.wrapper;

import com.youngboss.ccredisoper.serilize.Serializer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.util.List;

import static com.youngboss.ccredisoper.util.RedisoperUtil.doInPipeline;

/**
 * @author ybd
 * @date 19-4-28
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class WrapperRedisoper {

	private final RedisSerializer<String> stringSerializer = RedisSerializer.string();

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	@Qualifier("tranRedisTemplate")
	private RedisTemplate<String, Object> tranRedisTemplate;

	@Autowired
	private Serializer valueSerializer;

	public void set(String key, Object entity, long expireSecond) {
		Assert.notNull(entity, "Entity could not be null!");
		SerializeWrapper wrapper = SerializeWrapper.of(entity);
		tranRedisTemplate.execute((RedisCallback<Void>) connection -> {
			doInPipeline(connection, conn -> {
				if (expireSecond > 0) {
					conn.setEx(rawKey(key), expireSecond, rawWrapperValue(wrapper));
				} else {
					conn.set(rawKey(key), rawWrapperValue(wrapper));
				}

			});
			return null;
		});
	}

	@SuppressWarnings({"unchecked", "unused"})
	public <E> E getEntity(String key, Class<E> clazz) {
		return redisTemplate.execute((RedisCallback<E>) connection -> {
			SerializeWrapper wrapper = deserializeWrapper(connection.get(rawKey(key)));
			return wrapper == null ? null : (E) wrapper.getData();
		});
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> getList(String key, Class<E> clazz) {
		return redisTemplate.execute((RedisCallback<List<E>>) connection -> {
			SerializeWrapper wrapper = deserializeWrapper(connection.get(rawKey(key)));
			return wrapper == null ? null : (List<E>) wrapper.getData();
		});
	}

	private byte[] rawWrapperValue(@NonNull SerializeWrapper wrapper) {
		return valueSerializer.serialize(wrapper);
	}

	private SerializeWrapper deserializeWrapper(byte[] wrapperBytes) {
		return wrapperBytes == null ? null : valueSerializer.deserialize(wrapperBytes, SerializeWrapper.class);
	}

	private byte[] rawKey(String key) {
		return stringSerializer.serialize(key);
	}
}
