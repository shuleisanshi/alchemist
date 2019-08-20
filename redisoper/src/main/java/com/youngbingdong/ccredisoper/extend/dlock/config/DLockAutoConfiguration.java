package com.youngbingdong.ccredisoper.extend.dlock.config;

import com.youngbingdong.ccredisoper.extend.dlock.DLock;
import com.youngbingdong.ccredisoper.extend.dlock.impl.redisson.RedissonDLock;
import com.youngbingdong.ccredisoper.extend.dlock.impl.spring.SpringDataRedisDLock;
import com.youngbingdong.ccredisoper.config.RedisoperAutoConfiguration;
import com.youngbingdong.ccredisoper.extend.dlock.annotated.DLockAspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

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

	@ConditionalOnProperty(prefix = DLockProperty.PREFIX, value = "dLockType", havingValue = "spring")
	@Bean
	public RedisScript<Boolean> releaseLockScript() {
		DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
		String scriptLocation = property.getSpring().getScriptLocation();
		redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(scriptLocation)));
		redisScript.setResultType(Boolean.class);
		return redisScript;
	}
}
