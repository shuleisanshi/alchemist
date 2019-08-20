package com.youngbingdong.ccredisoper.extend.dlock;

import com.youngbingdong.ccredisoper.CcRedisoperApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
public class DLockServiceTest extends CcRedisoperApplicationTests {

	@Autowired
	private DLockService dLockService;

	@Test
	public void dLockAnnotationTest() {
		dLockService.lockTest(666L);
	}
}
