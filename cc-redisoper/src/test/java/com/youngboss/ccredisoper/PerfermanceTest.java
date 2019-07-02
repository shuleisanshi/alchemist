package com.youngboss.ccredisoper;

import com.youngboss.ccredisoper.core.UserService;
import com.youngboss.ccredisoper.vo.TestUser;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 19-3-22
 * @contact yangbingdong1994@gmail.com
 */
public class PerfermanceTest extends CcRedisoperApplicationTests {

	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	@Autowired
	private UserService userService;

	public static final TestUser YANGBINGDONG = TestUser.buildUserByName("yangbingdong");
	public static final TestUser YANGBINGDONG_TEMP = TestUser.buildUserByName("yangbingdong");

	/**
	 * QPS:
	 * 使用pipeline: 4600+
	 * 不使用pipeline: 3600+
	 */
	@Test
	@PerfTest(duration = 15000, threads = 30, warmUp = 3000)
	public void setAndGetTestUser() {
		userService.save(YANGBINGDONG);
		userService.update(YANGBINGDONG_TEMP.setName("yangbingdong1").setEmail("yangbingdongemail1"));
		userService.update(YANGBINGDONG_TEMP.setName("yangbingdong2").setEmail("yangbingdongemail2"));
		userService.update(YANGBINGDONG_TEMP.setName("yangbingdong3").setEmail("yangbingdongemail3"));
		userService.update(YANGBINGDONG_TEMP.setName("yangbingdong4").setEmail("yangbingdongemail4"));
	}
}
