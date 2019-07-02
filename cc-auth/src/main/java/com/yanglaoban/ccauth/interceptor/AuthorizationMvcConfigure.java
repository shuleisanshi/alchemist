package com.yanglaoban.ccauth.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

/**
 * @author ybd
 * @date 19-5-22
 * @contact yangbingdong1994@gmail.com
 */
@RequiredArgsConstructor
public class AuthorizationMvcConfigure implements WebMvcConfigurer {

	private final AuthorizationInterceptor authorizationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (Objects.nonNull(authorizationInterceptor)) {
			registry.addInterceptor(authorizationInterceptor);
		}
	}
}
