package com.youngbingdong.ccredisoper.extend.dlock.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author ybd
 * @date 19-5-16
 * @contact yangbingdong1994@gmail.com
 */
@Data
@EnableConfigurationProperties(RedisProperties.class)
@ConfigurationProperties(DLockProperty.PREFIX)
public class DLockProperty {

	public static final String PREFIX = "cc.redisoper.dlock";

	@Resource
	private RedisProperties redisProperties;

	private Boolean enable = false;

	private String host;

	private String port;

	private String password;

	/**
	 * 获取锁时最大等待时间
	 */
	private Long waitSecond = 2L;

	/**
	 * 锁释放时间，防止程序挂掉没有释放锁
	 */
	private Long leaseSecond = 2L;

	/**
	 * 分布式锁实现类型
	 */
	private DLockType dLockType = DLockType.REDISSON;

	private Spring spring = new Spring();

	@Data
	public static class Spring {
		private String scriptLocation = "scripts/release_lock.lua";
	}

	@PostConstruct
	public void init() {
		this.host = redisProperties.getHost();
		this.port = String.valueOf(redisProperties.getPort());
		this.password = redisProperties.getPassword();
	}
}
