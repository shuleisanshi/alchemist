package com.youngbingdong.redisoper.extend.commom;

import cn.hutool.core.util.StrUtil;
import com.youngbingdong.redisoper.RedisoperApplicationTests;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Index;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ybd
 * @date 19-5-15
 * @contact yangbingdong1994@gmail.com
 */
public class CommonRedisoperTest extends RedisoperApplicationTests {

	@Autowired
	private CommonRedisoper commonRedisoper;

	private static Set<String> keysToBeDelete = new HashSet<>(1 << 5);

	public void deleteAfterTest() {
		if (keysToBeDelete.size() > 0) {
			commonRedisoper.del(keysToBeDelete.toArray(new String[0]));
			keysToBeDelete.clear();
		}
	}

	@Test
	public void setNotExpireAndTTLTest() {
		String key = "setTestKey";
		String value = "setTestValue";
		keysToBeDelete.add(key);
		commonRedisoper.set(key, value, 0);

		String valueInRedis = commonRedisoper.get(key);
		Assertions.assertThat(valueInRedis)
				  .isNotNull()
				  .isEqualTo(value);

		Long ttl = commonRedisoper.ttl(key);
		Assertions.assertThat(ttl)
				  .isEqualTo(-1L);
	}

	@Test
	public void existAndExpireTest() {
		String key = "existAndExpireAtTest";
		String value = "existAndExpireAtTest";
		keysToBeDelete.add(key);
		commonRedisoper.set(key, value, 0);
		commonRedisoper.expireAt(key, System.currentTimeMillis() + 60 * 1000);
		Boolean exists = commonRedisoper.exists(key);
		Assertions.assertThat(exists)
				  .isTrue();
		Long ttl = commonRedisoper.ttl(key);
		Assertions.assertThat(ttl)
				  .isGreaterThan(50L);

		commonRedisoper.del(key);
		commonRedisoper.set(key, value, 0);
		commonRedisoper.expire(key, 60);
		ttl = commonRedisoper.ttl(key);
		Assertions.assertThat(ttl)
				  .isGreaterThan(50L);
	}

	@Test
	public void deleteTest() {
		String key = "deleteTest";
		String value = "deleteTest";
		keysToBeDelete.add(key);
		commonRedisoper.set(key, value, 0);
		Assertions.assertThat(commonRedisoper.exists(key))
				  .isTrue();
		commonRedisoper.del(key);
		Assertions.assertThat(commonRedisoper.exists(key))
				  .isFalse();

		commonRedisoper.set(key, value, 0);
		Assertions.assertThat(commonRedisoper.exists(key))
				  .isTrue();
		commonRedisoper.unlink(key);
		Assertions.assertThat(commonRedisoper.exists(key))
				  .isFalse();
	}

	@Test
	public void incrTest() {
		String key = "incrTest";
		keysToBeDelete.add(key);
		commonRedisoper.del(key);
		Long incr = commonRedisoper.incr(key);
		Assertions.assertThat(incr)
				  .isEqualTo(1L);
		commonRedisoper.del(key);
		commonRedisoper.set(key, "9", 0);
		incr = commonRedisoper.incr(key);
		Assertions.assertThat(incr)
				  .isEqualTo(10L);
		commonRedisoper.del(key);
		incr = commonRedisoper.incrBy(key, 10);
		Assertions.assertThat(incr)
				  .isEqualTo(10L);
	}

	@Test
	public void renameTest() {
		String key = "renameTest";
		String newKey = "renameTestNew";
		String value = "renameTest";
		keysToBeDelete.add(key);
		commonRedisoper.set(key, value, 0);
		commonRedisoper.rename(key, newKey);
		Assertions.assertThat(commonRedisoper.get(newKey))
				  .isNotNull()
				  .isEqualTo(value);
	}

	@Test
	public void setNxTest() {
		String key = "setNxTest";
		String value = "setNxTest";
		keysToBeDelete.add(key);
		commonRedisoper.del(key);
		Boolean nx = commonRedisoper.setNx(key, value, 60L);
		Assertions.assertThat(nx)
				  .isTrue();
		Assertions.assertThat(commonRedisoper.ttl(key))
				  .isGreaterThan(50L);
		nx = commonRedisoper.setNx(key, value, 60L);
		Assertions.assertThat(nx)
				  .isFalse();
	}

	@Test
	public void mGetTest() {
		String key1 = "mGetTest1";
		String value1 = "mGetTest1";
		String key2 = "mGetTest2";
		String value2 = "mGetTest2";
		keysToBeDelete.add(key1);
		keysToBeDelete.add(key2);
		commonRedisoper.set(key1, value1, 0);
		commonRedisoper.set(key2, value2, 0);
		List<String> values = commonRedisoper.mGet(Arrays.asList(key1, key2));
		Assertions.assertThat(values)
				  .hasSize(2)
				  .contains(value1, value2);
	}

	@Test
	public void listTest() {
		String key = "listTest";
		keysToBeDelete.add(key);
		List<String> listValue = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			listValue.add(String.valueOf(i));
		}
		commonRedisoper.lPush(key, listValue.toArray(new String[0]));
		List<String> range = commonRedisoper.lRange(key, 0, 49);
		Assertions.assertThat(range)
				  .hasSize(50)
				  .contains("49", Index.atIndex(0));

		commonRedisoper.lPush(key, "50");
		commonRedisoper.lTrim(key, 0, 49);
		range = commonRedisoper.lRange(key, 0, 49);
		Assertions.assertThat(range)
				  .hasSize(50)
				  .contains("50", Index.atIndex(0))
				  .contains("1", Index.atIndex(49));
	}

	@Test
	public void setTest() {
		String key = "setTest";
		keysToBeDelete.add(key);
		List<String> listValue = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			listValue.add(String.valueOf(i));
		}
		commonRedisoper.sAdd(key, listValue.toArray(new String[0]));
		Assertions.assertThat(commonRedisoper.sIsMember(key, "0"))
				  .isTrue();
		String pop = commonRedisoper.sPop(key);
		Assertions.assertThat(pop)
				  .isNotNull();
		Assertions.assertThat(Integer.valueOf(pop))
				  .isLessThan(50);
		Assertions.assertThat(commonRedisoper.sMembers(key))
				  .hasSize(49);
	}

    @Test
    public void batchExpireTest() {
        String key1 = "batchExpireTest1";
        String key2 = "batchExpireTest2";
        String key3 = "batchExpireTest3";

        List<String> strings = Arrays.asList(key1, key2, key3);
        Map<String, String> tuple = new HashMap<>();
        for (String string : strings) {
            tuple.put(string, StrUtil.EMPTY);
        }
        commonRedisoper.mSet(tuple);
        commonRedisoper.mExpire(strings, 60L);

        System.out.println(commonRedisoper.ttl(key1));
        System.out.println(commonRedisoper.ttl(key2));

        commonRedisoper.del(strings.toArray(new String[0]));
        System.out.println(commonRedisoper.ttl(key1));
    }


}