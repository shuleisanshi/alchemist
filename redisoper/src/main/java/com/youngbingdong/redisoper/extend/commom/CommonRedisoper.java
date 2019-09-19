package com.youngbingdong.redisoper.extend.commom;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.data.redis.connection.RedisStringCommands.SetOption.SET_IF_ABSENT;
import static org.springframework.data.redis.core.types.Expiration.from;

/**
 * @author ybd
 * @date 19-5-15
 * @contact yangbingdong1994@gmail.com
 *
 * 只支持String
 */
public class CommonRedisoper implements InitializingBean {

	private final RedisSerializer<String> stringSerializer = RedisSerializer.string();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	@Qualifier("tranRedisTemplate")
	private RedisTemplate<String, Object> tranRedisTemplate;

    private RedisScript script;

    @Override
    public void afterPropertiesSet() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/batch_expire.lua")));
        redisScript.setResultType(Boolean.class);
        script = redisScript;
    }


	/* ###################  KEY ################### */

	public void del(String... keys) {
		del(true, keys);
	}

	public void del(boolean enableTransaction, String... keys) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.del(rawKeys(keys));
			return null;
		});
	}

	public void unlink(String... keys) {
		unlink(true, keys);
	}

	public void unlink(boolean enableTransaction, String... keys) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.unlink(rawKeys(keys));
			return null;
		});
	}

	public void expire(String key, long timeout) {
		expire(true, key, timeout);
	}

	public void expire(boolean enableTransaction, String key, long timeout) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.expire(rawKey(key), timeout);
			return null;
		});
	}

	public void expireAt(String key, long timestampMillis) {
		expireAt(true, key, timestampMillis);
	}

	public void expireAt(boolean enableTransaction, String key, long timestampMillis) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.expireAt(rawKey(key), timestampMillis/1000);
			return null;
		});
	}

	public Long ttl(String key) {
		return redisTemplate.execute((RedisCallback<Long>) connection -> connection.ttl(rawString(key)));
	}

	public Boolean exists(String key) {
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(rawKey(key)));
	}

	public void rename(String oldKey, String newKey) {
		rename(true, oldKey, newKey);
	}

	public void rename(boolean enableTransaction, String oldKey, String newKey) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.rename(rawKey(oldKey), rawKey(newKey));
			return null;
		});
	}

    /**
     * 批量过期
     */
    public void mExpire(List<String> keys, Long expire) {
        //noinspection unchecked
        executeScript(script, keys, keys.size(), expire);
    }

	/* ################### String ################### */

	public void set(String key, String value, long timeout) {
		set(true, key, value, timeout);
	}

	public void set(boolean enableTransaction, String key, String value, long timeout) {
		Assert.isTrue(timeout >= 0, "Timeout must ge zero!");
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			if (timeout > 0) {
				connection.setEx(rawKey(key), timeout, rawValue(value));
			} else {
				connection.set(rawKey(key), rawValue(value));
			}
			return null;
		});
	}

    public void mSet(Map<String, String> tuple) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            Map<byte[], byte[]> byteTuple = new HashMap<>(tuple.size());
            tuple.forEach((k, v) -> {
                byteTuple.put(rawString(k), rawString(v));
            });
            connection.mSet(byteTuple);
            return null;
        });
    }

	public Boolean setNx(String key, String value, long timeOut) {
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set(rawKey(key), rawValue(value), from(timeOut, SECONDS), SET_IF_ABSENT));
	}

	public String get(String key) {
		return redisTemplate.execute((RedisCallback<String>) connection -> {
			byte[] value = connection.get(rawKey(key));
			return deserializeValue(value);
		});
	}

	public Long incr(String key){
		return redisTemplate.execute((RedisCallback<Long>) connection -> connection.incr(rawKey(key)));
	}

	public Long incrBy(String key, long incr){
		return incrBy(true, key, incr);
	}

	public Long incrBy(boolean enableTransaction, String key, long incr){
		return getRedisTemplate(enableTransaction).execute((RedisCallback<Long>) connection -> connection.incrBy(rawKey(key), incr));
	}

	public List<String> mGet(List<String> keyList) {
		return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
			List<byte[]> values = connection.mGet(rawKeys(keyList.toArray(new String[0])));
			return deserializeValues(values);
		});
	}

	/* ################### List ################### */

	public void lPush(String key, String... values) {
		lPush(true, key, values);
	}

	public void lPush(boolean enableTransaction, String key, String... values) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.lPush(rawKey(key), rawValues(values));
			return null;
		});
	}

	public List<String> lRange(String key, long start, long stop) {
		return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
			List<byte[]> values = connection.lRange(rawKey(key), start, stop);
			return deserializeValues(values);
		});
	}

	public void lTrim(String key, long start, long stop) {
		lTrim(true, key, start, stop);
	}

	public void lTrim(boolean enableTransaction, String key, long start, long stop) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.lTrim(rawKey(key), start, stop);
			return null;
		});
	}

	/* ################### SET ################### */

	public void sAdd(String key, String... values) {
		sAdd(true, key, values);
	}

	public void sAdd(boolean enableTransaction, String key, String... values) {
		getRedisTemplate(enableTransaction).execute((RedisCallback<Void>) connection -> {
			connection.sAdd(rawKey(key), rawValues(values));
			return null;
		});
	}

	public Set<String> sMembers(String key) {
		return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
			Set<byte[]> values = connection.sMembers(rawKey(key));
			return deserializeValues(values);
		});
	}

	public String sPop(String key) {
		return redisTemplate.execute((RedisCallback<String>) connection -> deserializeValue(connection.sPop(rawKey(key))));
	}

	public Boolean sIsMember(String key, String member) {
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.sIsMember(rawKey(key), rawValue(member)));
	}

	/* ################### Script ################### */

	@SuppressWarnings({"unchecked"})
    public <T> T executeScript(RedisScript<T> script, List<String> keys, Object... args) {
        args = Stream.of(args)
                     .map(Object::toString)
                     .toArray();
        return redisTemplate.execute(script, stringSerializer, (RedisSerializer<T>) stringSerializer, keys, args);
	}

	/* ################### Serialize ################### */

	private byte[] rawString(String key) {
		return stringSerializer.serialize(key);
	}

	private byte[][] rawStrings(String... strings) {
		byte[][] rawStrings = new byte[strings.length][];
		int i = 0;
		for (String s : strings) {
			rawStrings[i++] = rawValue(s);
		}
		return rawStrings;
	}

	private byte[] rawKey(String key) {
		return rawString(key);
	}

	private byte[][] rawKeys(String... keys) {
		return rawStrings(keys);
	}

	private byte[] rawValue(String value) {
		return rawString(value);
	}

	private byte[][] rawValues(String... values) {
		return rawStrings(values);
	}

	private String deserializeValue(byte[] valueBytes) {
		return stringSerializer.deserialize(valueBytes);
	}

	private Set<String> deserializeValues(Set<byte[]> valuesBytes) {
		if (valuesBytes == null) {
			return Collections.emptySet();
		}
		Set<String> values = new HashSet<>(valuesBytes.size());
		for (byte[] valuesByte : valuesBytes) {
			values.add(deserializeValue(valuesByte));
		}
		return values;
	}

	private List<String> deserializeValues(List<byte[]> valuesBytes) {
		if (valuesBytes == null) {
			return Collections.emptyList();
		}
		List<String> values = new ArrayList<>(valuesBytes.size());
		for (byte[] valuesByte : valuesBytes) {
			values.add(deserializeValue(valuesByte));
		}
		return values;
	}

	private RedisTemplate<String, Object> getRedisTemplate(boolean enableTransaction) {
		return enableTransaction ? tranRedisTemplate : redisTemplate;
	}

}
