package com.youngbingdong.ccredisoper.core;

import com.youngbingdong.ccredisoper.CcRedisoperApplicationTests;
import com.youngbingdong.ccredisoper.vo.TestUser;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author ybd
 * @date 19-3-21
 * @contact yangbingdong1994@gmail.com
 */
public class GenericRedisoperTest extends CcRedisoperApplicationTests {

	@Autowired
	private UserService userService;
	public static final String EMAIL = "yangbingdong1994@gmail.com";
	public static final String NAME = "yangbingdong";

	@After
	public void clean() {
		userService.getRedisOperator().deleteAllInRedis();
	}

	@Test
	public void setAndGetTestUser() {
		TestUser byId = userService.getById(5555555555L);
		Assertions.assertThat(byId)
				  .isNotNull()
				  .extracting(TestUser::getEmail)
				  .isEqualTo(EMAIL);

		TestUser byId2 = userService.getById(5555555555L);
		Assertions.assertThat(byId2)
				  .isNotNull()
				  .extracting(TestUser::getId)
				  .isEqualTo(5555555555L);

		TestUser byEmail = userService.getByEmail(EMAIL);
		Assertions.assertThat(byEmail)
				  .isNotNull()
				  .extracting(TestUser::getEmail)
				  .isEqualTo(EMAIL);

		List<TestUser> byName = userService.getByName(NAME);
		Assertions.assertThat(byName)
				  .isNotEmpty()
				  .hasSize(1);
		Assertions.assertThat(byName.get(0))
				  .isNotNull()
				  .extracting(TestUser::getName)
				  .isEqualTo(NAME);

		String email2 = "ookamid.com";
		userService.update(byEmail.setEmail(email2));
		TestUser byEmail2 = userService.getByEmail(email2);
		Assertions.assertThat(byEmail2)
				  .isNotNull()
				  .extracting(TestUser::getEmail)
				  .isEqualTo(email2);

		List<TestUser> byName2 = userService.getByName(NAME);
		Assertions.assertThat(byName2)
				  .isNotEmpty()
				  .hasSize(1);
		Assertions.assertThat(byName2.get(0))
				  .isNotNull()
				  .extracting(TestUser::getName)
				  .isEqualTo(NAME);

		userService.getRedisOperator().deleteInRedis(byName2.get(0));
	}

	@Test
	public void batchSet2RedisTest() {
		TestUser user1 = TestUser.buildUserByName(NAME).setEmail(EMAIL).setId(1L);
		TestUser user2 = TestUser.buildUserByName(NAME + "2").setEmail(EMAIL + "2").setId(2L);
		TestUser user3 = TestUser.buildUserByName(NAME + "3").setEmail(EMAIL + "3").setId(3L);
		GenericRedisoper<TestUser> redisOperator = userService.getRedisOperator();
		redisOperator.batchSet2Redis(Arrays.asList(user1, user2, user3));

		user1 = redisOperator.getByUniqueIndex(null, TestUser.UNIQUE_IDX_EMAIL, EMAIL);
		Assertions.assertThat(user1)
				  .isNotNull()
				  .extracting(TestUser::getId)
				  .isEqualTo(1L);

		user2 = redisOperator.getByKey(null, 2L);
		Assertions.assertThat(user2)
				  .isNotNull()
				  .extracting(TestUser::getEmail)
				  .isEqualTo(EMAIL + "2");

		List<TestUser> user3ByName = redisOperator.getByNormalIndex(null, TestUser.NORMAL_IDX_NAME, NAME + "3");
		Assertions.assertThat(user3ByName)
				  .isNotEmpty()
				  .hasSize(1);
		user3 = user3ByName.get(0);
		Assertions.assertThat(user3)
				  .isNotNull()
				  .extracting(TestUser::getName)
				  .isEqualTo(NAME + "3");

		Assertions.assertThat(user3.getEmail())
				  .isNotNull()
				  .isEqualTo(EMAIL + "3");
	}

	@Test
	public void incrIdTest() {
		Long id = userService.getRedisOperator().incrId(() -> 10L);
		Assertions.assertThat(id)
				  .isEqualTo(10L);

		id = userService.getRedisOperator().incrId(() -> 10L);
		Assertions.assertThat(id)
				  .isEqualTo(11L);
	}

	@Test
	public void getIdValue() {
		TestUser user = TestUser.buildUserByName(NAME).setEmail(EMAIL).setId(1L);
		Long idValue = userService.getIdValue(user);
		Assertions.assertThat(idValue)
				  .isEqualTo(1L);
	}

	@Test
	public void injectTest() {
		TestUser user = TestUser.buildUserByName(NAME).setEmail(EMAIL).setId(1L);
		userService.getRedisOperator().getEntityMetadata().injectPrimaryValue(user, 2L);
		Assertions.assertThat(user.getId())
				  .isEqualTo(2L);

	}

}