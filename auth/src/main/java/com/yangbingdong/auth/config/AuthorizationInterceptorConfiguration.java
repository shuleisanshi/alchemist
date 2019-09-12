package com.yangbingdong.auth.config;

import com.yangbingdong.auth.interceptor.AuthorizationInterceptor;
import com.yangbingdong.auth.interceptor.AuthorizationMvcConfigure;
import com.yangbingdong.auth.interceptor.handler.AuthorizationPreHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * @author ybd
 * @date 19-5-22
 * @contact yangbingdong1994@gmail.com
 */
@ConditionalOnBean(AuthorizationPreHandler.class)
public class AuthorizationInterceptorConfiguration {

	@Bean
	public AuthorizationInterceptor authorizationInterceptor(
			ObjectProvider<AuthorizationPreHandler> authorizationHandlerObjectProvider) {
		return new AuthorizationInterceptor(authorizationHandlerObjectProvider.getIfAvailable());
	}

	@Order(1)
	@Bean
	public AuthorizationMvcConfigure authorizationMvcConfigure(
			ObjectProvider<AuthorizationInterceptor> authorizationInterceptorObjectProvider) {
		return new AuthorizationMvcConfigure(authorizationInterceptorObjectProvider.getIfAvailable());
	}
}
