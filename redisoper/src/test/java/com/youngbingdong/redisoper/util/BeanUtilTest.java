package com.youngbingdong.redisoper.util;

import com.youngbingdong.redisoper.vo.TestOtherUser;
import com.youngbingdong.redisoper.vo.TestUser;
import com.youngbingdong.util.reflect.BeanUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author ybd
 * @date 19-3-25
 * @contact yangbingdong1994@gmail.com
 */
public class BeanUtilTest {

	@Test
	public void copyPropertiesIgnoreNull() {
		TestUser oldUser = new TestUser().setName("ybd")
										 .setAge(18)
										 .setEmail("666");

		TestUser newUser = new TestUser().setName("yangbingdong");

		BeanUtil.copyPropertiesWithSameClass(newUser, oldUser);

		Assertions.assertThat(oldUser.getName())
				  .isEqualTo("yangbingdong");

		Assertions.assertThat(oldUser.getEmail())
				  .isNotNull();
	}

	@Test
	public void copyDifferentClassObjectExpectException() {
		TestUser oldUser = new TestUser().setName("ybd");
		TestOtherUser testOtherUser = new TestOtherUser().setName("yangbingdong");

		Assertions.assertThatThrownBy(() -> BeanUtil.copyPropertiesWithSameClass(testOtherUser, oldUser))
				  .isInstanceOf(IllegalArgumentException.class)
				  .hasMessage("Not support different class");
	}
}