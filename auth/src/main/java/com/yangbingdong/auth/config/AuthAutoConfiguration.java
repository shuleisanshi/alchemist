package com.yangbingdong.auth.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yangbingdong.auth.jwt.JwtOperator;
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
@Import(AuthorizationInterceptorConfiguration.class)
@EnableConfigurationProperties(AuthProperty.class)
public class AuthAutoConfiguration {

	@Bean
	public JwtOperator jwtOperator(AuthProperty authProperty) {
		Cache<String, Long> cache = null;
		if (authProperty.isEnableJwtSession()) {
			cache = Caffeine.newBuilder()
                            .expireAfterAccess(authProperty.getLocalSessionExpireSecond(), SECONDS)
                            .expireAfterWrite(authProperty.getLocalSessionExpireSecond(), SECONDS)
							.maximumSize(authProperty.getLocalSessionCacheMaximumSize())
							.build();
		}
		return new JwtOperator(cache);
	}
}
