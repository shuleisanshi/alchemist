package com.youngbingdong.redisoper.extend.dlock;

import com.youngbingdong.redisoper.RedisoperApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
public class DLockServiceTest extends RedisoperApplicationTests {

	@Autowired
	private DLockService dLockService;

	@Test
	public void dLockAnnotationTest() {
		dLockService.lockTest(666L);
	}
}
