package com.youngboss.ccredisoper.core;

import com.youngboss.ccredisoper.vo.TestUser;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.youngboss.ccredisoper.core.UserNoRepoService.*;

/**
 * @author ybd
 * @date 19-3-26
 * @contact yangbingdong1994@gmail.com
 */
public class UserNoRepoServiceTest extends GenericRedisoperTest {

	@Autowired
	private UserNoRepoService userNoRepoService;

	@After
	public void clean() {
		userNoRepoService.getRedisOperator().deleteAllInRedis();
	}

	@Test
	public void setAndGetTestUser() {
		TestUser yangbingdong = TestUser.buildUserByName(NAME).setEmail(EMAIL).setId(null);

		yangbingdong = userNoRepoService.save(yangbingdong);
		Assertions.assertThat(yangbingdong)
				  .isNotNull()
				  .extracting(TestUser::getId)
				  .isEqualTo(ID);

		yangbingdong = userNoRepoService.getById(ID);
		Assertions.assertThat(yangbingdong)
				  .isNotNull()
				  .extracting(TestUser::getEmail)
				  .isEqualTo(EMAIL);

		yangbingdong = userNoRepoService.getByEmail(EMAIL);
		Assertions.assertThat(yangbingdong)
				  .isNotNull()
				  .extracting(TestUser::getEmail)
				  .isEqualTo(EMAIL);

		List<TestUser> yangbingdongs = userNoRepoService.getByName(NAME);
		Assertions.assertThat(yangbingdongs)
				  .isNotEmpty()
				  .hasSize(1);
		Assertions.assertThat(yangbingdongs.get(0))
				  .isNotNull()
				  .extracting(TestUser::getName)
				  .isEqualTo(NAME);

		String name2 = "ookamid";
		String email2 = "ookamid.com";
		userNoRepoService.update(yangbingdong.setName(name2).setEmail(email2));
		yangbingdong = userNoRepoService.getByEmail(email2);
		Assertions.assertThat(yangbingdong)
				  .isNotNull()
				  .extracting(TestUser::getId)
				  .isEqualTo(ID);

		yangbingdongs = userNoRepoService.getByName(name2);
		Assertions.assertThat(yangbingdongs)
				  .isNotEmpty()
				  .hasSize(1);
		Assertions.assertThat(yangbingdongs.get(0))
				  .isNotNull()
				  .extracting(TestUser::getName)
				  .isEqualTo(name2);

		yangbingdong = userNoRepoService.getById(ID);
		Assertions.assertThat(yangbingdong)
				  .isNotNull()
				  .extracting(TestUser::getEmail)
				  .isEqualTo(email2);
	}


}