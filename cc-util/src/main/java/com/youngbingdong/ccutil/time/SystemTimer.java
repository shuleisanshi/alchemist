package com.youngbingdong.ccutil.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.locks.LockSupport;

import static java.time.Instant.ofEpochMilli;

/**
 * @author ybd
 * @date 19-4-15
 * @contact yangbingdong1994@gmail.com
 */
public final class SystemTimer {
	private static final ZoneId SYSTEM_DEFAULT_ZONE = ZoneId.systemDefault();
	private static final SystemTimer INSTANCE = new SystemTimer();

	private long now = System.currentTimeMillis();
	private LocalDateTime localDateTime = LocalDateTime.ofInstant(ofEpochMilli(now), SYSTEM_DEFAULT_ZONE);

	private SystemTimer() {
		Thread updater = new Thread("SystemTimer Updater Thread") {
			@SuppressWarnings("InfiniteLoopStatement")
			@Override
			public void run() {
				while (true) {
					now = System.currentTimeMillis();
					localDateTime = LocalDateTime.ofInstant(ofEpochMilli(now), SYSTEM_DEFAULT_ZONE);
					LockSupport.parkNanos(1000_000);
				}
			}
		};
		updater.setDaemon(true);
		updater.start();
	}

	public static long now() {
		return INSTANCE.currentTimeMillis();
	}

	private long currentTimeMillis() {
		return now;
	}

	private LocalDateTime currentLocalDateTime() {
		return localDateTime;
	}

	public static LocalDateTime nowDateTime() {
		return INSTANCE.currentLocalDateTime();
	}
}
