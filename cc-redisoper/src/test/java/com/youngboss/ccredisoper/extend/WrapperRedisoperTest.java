package com.youngboss.ccredisoper.extend;

import com.youngboss.ccredisoper.CcRedisoperApplicationTests;
import com.youngboss.ccredisoper.extend.wrapper.WrapperRedisoper;
import com.youngboss.ccredisoper.vo.TestUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ybd
 * @date 19-4-28
 * @contact yangbingdong1994@gmail.com
 */
public class WrapperRedisoperTest extends CcRedisoperApplicationTests {

	@Autowired
	private WrapperRedisoper wrapperRedisoper;


	@Test
	public void getDateTest() {
		String key = "GetDateTest";
		TestUser user = TestUser.buildUserById(1L);
		wrapperRedisoper.set(key, user, 60);
		TestUser data = wrapperRedisoper.getEntity(key, TestUser.class);
		Assertions.assertThat(data)
				  .isEqualTo(user);
	}

	@Test
	public void getListTest() {
		String key = "GetListTest";
		int size = 100;
		List<TestUser> userList = IntStream.range(0, size)
										   .mapToObj(i -> TestUser.buildUserById(1L))
										   .collect(Collectors.toList());
		wrapperRedisoper.set(key, userList, 60);
		List<TestUser> list = wrapperRedisoper.getList(key, TestUser.class);
		Assertions.assertThat(list)
				  .hasSize(size);
	}
}