package com.youngboss.ccredisoper.core;

import com.youngboss.ccredisoper.vo.TestUser;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.youngboss.ccredisoper.core.GenericRedisoperTest.EMAIL;
import static com.youngboss.ccredisoper.core.GenericRedisoperTest.NAME;

/**
 * @author ybd
 * @date 19-3-21
 * @contact yangbingdong1994@gmail.com
 */
@Service
public class UserService implements RedisoperAware<TestUser> {

	private TestUserRepository testUserRepository = new TestUserRepository();

	@Getter
	private GenericRedisoper<TestUser> redisOperator;

	@Override
	public void setRedisoper(GenericRedisoper<TestUser> redisoper) {
		this.redisOperator = redisoper;
	}

	public TestUser getById(Long id) {
		return redisOperator.getByKey(() -> testUserRepository.getById(id), id);
	}

	public TestUser getByEmail(String email) {
		return redisOperator.getByUniqueIndex(() -> testUserRepository.getByEmail(email), TestUser.UNIQUE_IDX_EMAIL, email);
	}

	public List<TestUser> getByName(String name) {
		return redisOperator.getByNormalIndex(() -> testUserRepository.getByName(name), TestUser.NORMAL_IDX_NAME, name);
	}

	public TestUser save(TestUser testUser) {
		saveOrUpdateInnerDB(testUser);
		redisOperator.set2Redis(testUser);
		return testUser;
	}

	public void update(TestUser testUser) {
		redisOperator.updateInRedis(null, testUser);
	}

	public Long getIdValue(TestUser testUser) {
		return (Long) redisOperator.getEntityMetadata().getPrimaryValue(testUser);
	}

	private void saveOrUpdateInnerDB(TestUser testUser) {
		if (testUser.getId() == null) {
			testUser.setId(6666666666666666666L);
		}
	}

	class TestUserRepository {
		TestUser getById(Long id) {
			return TestUser.buildUserById(id).setEmail(EMAIL).setName(NAME);
		}

		TestUser getByEmail(String email) {
			return TestUser.buildUserByName(NAME).setEmail(EMAIL);
		}

		public List<TestUser> getByName(String name) {
			return Collections.singletonList(TestUser.buildUserByName(NAME).setEmail(EMAIL));
		}
	}
}
