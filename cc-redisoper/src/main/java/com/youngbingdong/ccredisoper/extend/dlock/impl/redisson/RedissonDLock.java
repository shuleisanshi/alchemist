package com.youngbingdong.ccredisoper.extend.dlock.impl.redisson;

import com.youngbingdong.ccredisoper.extend.dlock.DLock;
import com.youngbingdong.ccredisoper.extend.dlock.AfterAcquireCommand;
import com.youngbingdong.ccredisoper.extend.dlock.config.DLockProperty;
import io.netty.channel.epoll.Epoll;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;

import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 19-5-16
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class RedissonDLock implements DLock {

	private Long defaultWaitSecond;

	private Long defaultLeaseSecond;

	private final RedissonClient redisson;

	public RedissonDLock(DLockProperty property) {
		this.defaultWaitSecond = property.getWaitSecond();
		this.defaultLeaseSecond = property.getLeaseSecond();

		Config config = new Config();
		config.setLockWatchdogTimeout(10000);
		SingleServerConfig singleServerConfig = config.useSingleServer();
		singleServerConfig.setAddress("redis://" + property.getHost() + ":" + property.getPort());
		if (property.getPassword() != null && property.getPassword().trim().length() > 0) {
			singleServerConfig.setPassword(property.getPassword());
		}
		try {
			Class.forName("io.netty.channel.epoll.Epoll");
			if (Epoll.isAvailable()) {
				config.setTransportMode(TransportMode.EPOLL);
				log.info("Starting with optional epoll library");
			} else {
				log.info("Starting without optional epoll library");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		redisson = Redisson.create(config);
	}

	@Override
	public <T> T tryLock(String lockKey, AfterAcquireCommand<T> afterAcquireCommand) throws Throwable {
		return tryLock(lockKey, defaultWaitSecond, defaultLeaseSecond, afterAcquireCommand);
	}

	@Override
	public <T> T tryLock(String lockKey, long waitSecond, long leaseSecond, AfterAcquireCommand<T> command) throws Throwable {
		RLock lock = redisson.getLock(lockKey);
		try {
			if (lock.tryLock(waitSecond, leaseSecond, TimeUnit.SECONDS)) {
				return command.executeCommand();
			}
			return command.fallback();
		} finally {
			lock.unlockAsync();
		}
	}
}
