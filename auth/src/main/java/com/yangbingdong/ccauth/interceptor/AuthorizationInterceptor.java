package com.yangbingdong.ccauth.interceptor;

import com.yangbingdong.ccauth.annotated.IgnoreAuth;
import com.yangbingdong.ccauth.interceptor.handler.AuthorizationPreHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

	private AuthorizationPreHandler authorizationPreHandler;

	public AuthorizationInterceptor(AuthorizationPreHandler authorizationPreHandler) {
		this.authorizationPreHandler = authorizationPreHandler;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();
			if (method.isAnnotationPresent(IgnoreAuth.class)) {
				return true;
			}
			authorizationPreHandler.preHandleAuth(request, response, method);
		}
		return true;
	}

}
