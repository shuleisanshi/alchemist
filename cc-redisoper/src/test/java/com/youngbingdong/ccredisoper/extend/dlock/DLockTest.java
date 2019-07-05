package com.youngbingdong.ccredisoper.extend.dlock;

import com.youngbingdong.ccredisoper.CcRedisoperApplicationTests;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author ybd
 * @date 19-5-16
 * @contact yangbingdong1994@gmail.com
 */
@Ignore
public class DLockTest extends CcRedisoperApplicationTests {
	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	private static int count = 0;

	private static LongAdder longAdder = new LongAdder();

	@Autowired
	private DLock dLock;

	@Autowired
	private DLockService dLockService;

	@Test
	@PerfTest(duration = 10000, threads = 6, warmUp = 500)
	public void dLockTest() throws Throwable {
		dLock.tryLock("dLockTestKey", (AfterAcquireCommand<Void>) () -> {
			longAdder.increment();
			count++;
			return null;
		});
	}

	@PerfTest(duration = 10000, threads = 50, warmUp = 500)
	@Test
	public void dLockAnnotationTest() {
		dLockService.lockTest(666L);
	}

	@AfterClass
	public static void destroy() {
		System.out.println("--------------------------  total: " + longAdder.longValue() + "  success: " + count);
	}

	@PreDestroy
	public void print() {
		System.out.println("--------------------------  total: " + longAdder.longValue() + "  success: " + count);
	}

}