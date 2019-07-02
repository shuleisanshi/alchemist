package com.youngboss.ccredisoper.util;

import com.youngboss.ccredisoper.vo.TestOtherUser;
import com.youngboss.ccredisoper.vo.TestUser;
import com.youngboss.ccutil.reflect.BeanUtil;
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

		BeanUtil.copyPropertiesIgnoreNull(newUser, oldUser);

		Assertions.assertThat(oldUser.getName())
				  .isEqualTo("yangbingdong");

		Assertions.assertThat(oldUser.getEmail())
				  .isNotNull();
	}

	@Test
	public void copyDifferentClassObjectExpectException() {
		TestUser oldUser = new TestUser().setName("ybd");
		TestOtherUser testOtherUser = new TestOtherUser().setName("yangbingdong");

		Assertions.assertThatThrownBy(() -> BeanUtil.copyPropertiesIgnoreNull(testOtherUser, oldUser))
				  .isInstanceOf(IllegalArgumentException.class)
				  .hasMessage("Not support different class");
	}
}