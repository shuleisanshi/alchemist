package com.yanglaoban.ccauth.interceptor.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ybd
 * @date 19-5-22
 * @contact yangbingdong1994@gmail.com
 */
public interface AuthorizationPreHandler {

	void preHandleAuth(HttpServletRequest request, HttpServletResponse response, Method method);
}
