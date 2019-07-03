package com.yangbingdong.ccauth.interceptor.handler;

import com.yangbingdong.ccauth.interceptor.AuthContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 19-5-23
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class DefaultAuthorizationPreHandler extends AbstractJwtAuthorizationPreHandler {

	@Override
	protected void preHandler(AuthContext authContext) {
		log.info("jwt body subject: {}", authContext.getSubject());
	}
}
