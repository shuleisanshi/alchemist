package com.yanglaoban.ccauth.config;

import com.yanglaoban.ccauth.interceptor.AuthorizationInterceptor;
import com.yanglaoban.ccauth.interceptor.AuthorizationMvcConfigure;
import com.yanglaoban.ccauth.interceptor.handler.AuthorizationPreHandler;
import com.yanglaoban.ccauth.interceptor.handler.DefaultAuthorizationPreHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * @author ybd
 * @date 19-5-22
 * @contact yangbingdong1994@gmail.com
 */
public class AuthorizationInterceptorConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AuthorizationPreHandler authorizationHandler() {
		return new DefaultAuthorizationPreHandler();
	}

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
