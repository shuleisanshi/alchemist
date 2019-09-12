package com.yangbingdong.example.controller;

import com.yangbingdong.auth.interceptor.AuthContext;
import com.yangbingdong.auth.interceptor.handler.AbstractJwtAuthorizationPreHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 19-5-23
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
@Component
public class MyAuthorizationPreHandler extends AbstractJwtAuthorizationPreHandler<MyJwtPayload> {

	@Override
	protected void preHandle(AuthContext<MyJwtPayload> authContext) {
		log.info("Jwt: {}", authContext.getJwt());
	}
}
