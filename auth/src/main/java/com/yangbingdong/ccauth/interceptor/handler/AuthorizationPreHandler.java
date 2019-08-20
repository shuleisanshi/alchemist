package com.yangbingdong.ccauth.interceptor.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ybd
 * @date 19-5-22
 * @contact yangbingdong1994@gmail.com
 */
public interface AuthorizationPreHandler {

	/**
	 * you can put user info hear, or do other logic.
	 * invoke by {@link AbstractJwtAuthorizationPreHandler#preHandleAuth(HttpServletRequest, HttpServletResponse, Method)}
	 */
	void preHandleAuth(HttpServletRequest request, HttpServletResponse response, Method method);
}
