package com.yangbingdong.ccauth.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yangbingdong.ccauth.jwt.JwtOperator;
import com.youngbingdong.ccredisoper.config.RedisoperAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author ybd
 * @date 19-5-21
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@AutoConfigureAfter(RedisoperAutoConfiguration.class)
@Import(AuthorizationInterceptorConfiguration.class)
@EnableConfigurationProperties(CcAuthProperty.class)
public class CcAuthAutoConfiguration {

	@Bean
	public JwtOperator jwtOperator(CcAuthProperty ccAuthProperty) {
		Cache<String, Long> cache = null;
		if (ccAuthProperty.isEnableJwtSession()) {
			cache = Caffeine.newBuilder()
							.expireAfterWrite(ccAuthProperty.getLocalSessionExpireSecond(), SECONDS)
							.maximumSize(ccAuthProperty.getLocalSessionCacheMaximumSize())
							.build();
		}
		return new JwtOperator(cache);
	}
}
