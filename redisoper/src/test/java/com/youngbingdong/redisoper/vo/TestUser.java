package com.youngbingdong.redisoper.vo;

import com.youngbingdong.redisoper.core.metadata.annotation.RedisIndex;
import com.youngbingdong.redisoper.core.metadata.annotation.RedisPrimaryKey;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;

@Data
@Accessors(chain = true)
public class TestUser implements Serializable {
	private static final long serialVersionUID = -2518364902214511991L;

	public static final Boolean containIndex = Boolean.TRUE;

	public static final String NORMAL_IDX_NAME = "IDX_NAME";
	public static final String UNIQUE_IDX_EMAIL = "UNIQUE_IDX_EMAIL";

	public static void main(String[] args) {
		Field[] fields = TestUser.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Resource.class)) {
				Resource declaredAnnotation = field.getDeclaredAnnotation(Resource.class);
			}
			System.out.println(field.getName());
		}
	}

	@RedisPrimaryKey
	private Long id;
	@RedisIndex(name = NORMAL_IDX_NAME)
	private String name;
	@RedisIndex(name = UNIQUE_IDX_EMAIL, unique = true)
	private String email;
	private Integer age;
	private TestSex sex;
	private LocalDateTime createTime;
	private Date updateTime;
	private List<TestUser> friends;

	public static TestUser buildUserByName(String name) {
		return new TestUser().setAge(20)
							 .setId(1111111111111111111L)
							 .setCreateTime(LocalDateTime.now())
							 .setUpdateTime(new Date())
							 .setEmail("6666666.com")
							 .setName(name)
							 .setSex(TestSex.MAIL)
							 .setFriends(singletonList(new TestUser().setAge(19)
																	 .setEmail("77777777.com")
																	 .setName("yqy")
																	 .setSex(TestSex.MAIL)));
	}

	public static TestUser buildUserById(Long id) {
		return new TestUser().setAge(20)
							 .setId(id)
							 .setCreateTime(LocalDateTime.now())
							 .setUpdateTime(new Date())
							 .setEmail("6666666.com")
							 .setName("yangbingdong")
							 .setSex(TestSex.MAIL)
							 .setFriends(singletonList(new TestUser().setAge(19)
																	 .setEmail("77777777.com")
																	 .setName("yqy")
																	 .setSex(TestSex.MAIL)));
	}
}
