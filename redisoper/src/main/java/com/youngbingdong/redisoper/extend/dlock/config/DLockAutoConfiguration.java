package com.youngbingdong.redisoper.extend.dlock.config;

import com.youngbingdong.redisoper.config.RedisoperAutoConfiguration;
import com.youngbingdong.redisoper.extend.dlock.DLock;
import com.youngbingdong.redisoper.extend.dlock.annotated.DLockAspect;
import com.youngbingdong.redisoper.extend.dlock.impl.redisson.RedissonDLock;
import com.youngbingdong.redisoper.extend.dlock.impl.spring.SpringDataRedisDLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author ybd
 * @date 19-5-16
 * @contact yangbingdong1994@gmail.com
 */
@AutoConfigureAfter(RedisoperAutoConfiguration.class)
@ConditionalOnProperty(prefix = DLockProperty.PREFIX, name = "enable", havingValue = "true")
@Configuration
@EnableConfigurationProperties(DLockProperty.class)
public class DLockAutoConfiguration {

	@Resource
	private DLockProperty property;

	@Bean
	public DLockAspect dLockAspect() {
		return new DLockAspect();
	}

	@ConditionalOnMissingBean(DLock.class)
	@Bean
	public DLock springDataRedisDLock() {
		switch (property.getDLockType()) {
			case REDISSON:
				return new RedissonDLock(property);
			case SPRING:
				return new SpringDataRedisDLock(property);
			default:
				return null;
		}
	}
}
