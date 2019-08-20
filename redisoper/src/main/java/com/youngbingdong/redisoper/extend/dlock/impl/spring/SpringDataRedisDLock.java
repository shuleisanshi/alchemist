package com.youngbingdong.redisoper.extend.dlock.impl.spring;

import com.youngbingdong.redisoper.extend.commom.CommonRedisoper;
import com.youngbingdong.redisoper.extend.dlock.AfterAcquireCommand;
import com.youngbingdong.redisoper.extend.dlock.DLock;
import com.youngbingdong.redisoper.extend.dlock.config.DLockProperty;
import com.youngbingdong.util.time.SystemTimer;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

/**
 * @author ybd
 * @date 19-5-16
 * @contact yangbingdong1994@gmail.com
 */
public class SpringDataRedisDLock implements DLock {

	@Resource
	private CommonRedisoper commonRedisoper;

	@Resource
	private RedisScript<Boolean> script;

	private Long defaultWaitSecond;

	private Long defaultLeaseSecond;

	public SpringDataRedisDLock(DLockProperty property) {
		this.defaultWaitSecond = property.getWaitSecond();
		this.defaultLeaseSecond = property.getLeaseSecond();
	}

	@Override
	public <T> T tryLock(String lockKey, AfterAcquireCommand<T> afterAcquireCommand) throws Throwable {
		return tryLock(lockKey, defaultWaitSecond, defaultLeaseSecond, afterAcquireCommand);
	}

	@Override
	public <T> T tryLock(String lockKey, long waitSecond, long leaseSecond, AfterAcquireCommand<T> command) throws Throwable {
		String uuid = UUID.randomUUID().toString();
		try {
			long begin = SystemTimer.now();
			long waitTimeMillis = TimeUnit.SECONDS.toMillis(waitSecond);
			while ((SystemTimer.now() - begin) < waitTimeMillis) {
				if (commonRedisoper.setNx(lockKey, uuid, leaseSecond)) {
					return command.executeCommand();
				}
				sleep();
			}
			return command.fallback();
		} finally {
			commonRedisoper.executeScript(script, singletonList(lockKey), uuid);
		}
	}

	private void sleep() {
		try {
			TimeUnit.MICROSECONDS.sleep(1);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
