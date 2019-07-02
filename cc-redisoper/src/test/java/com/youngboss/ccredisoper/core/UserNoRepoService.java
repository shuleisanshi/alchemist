package com.youngboss.ccredisoper.core;

import com.youngboss.ccredisoper.vo.TestUser;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ybd
 * @date 19-3-21
 * @contact yangbingdong1994@gmail.com
 */
@Service
public class UserNoRepoService implements RedisoperAware<TestUser> {

	public static final long ID = 6666666666666666666L;

	@Getter
	private GenericRedisoper<TestUser> redisOperator;

	@Override
	public void setRedisoper(GenericRedisoper<TestUser> redisoper) {
		this.redisOperator = redisoper;
	}

	public TestUser getById(Long id) {
		return redisOperator.getByKey(null, id);
	}

	public TestUser getByEmail(String email) {
		return redisOperator.getByUniqueIndex(null, TestUser.UNIQUE_IDX_EMAIL, email);
	}

	public List<TestUser> getByName(String name) {
		return redisOperator.getByNormalIndex(null, TestUser.NORMAL_IDX_NAME, name);
	}

	public TestUser save(TestUser testUser) {
		saveOrUpdateInnerDB(testUser);
		redisOperator.set2Redis(testUser);
		return testUser;
	}

	public void update(TestUser testUser) {
		redisOperator.updateInRedis(null, testUser);
	}

	private void saveOrUpdateInnerDB(TestUser testUser) {
		if (testUser.getId() == null) {
			testUser.setId(ID);
		}
		System.out.println("假装保存了");
	}

}
