package com.yangbingdong.mvc.interceptor;

import com.youngbingdong.util.IpUtil;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ybd
 * @date 18-5-22
 * @contact yangbingdong1994@gmail.com
 */
public class IpInterceptor extends HandlerInterceptorAdapter {

	public static final String IP = "IP";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String realIp = IpUtil.realIp(request);
		MDC.put(IP, realIp);
		request.setAttribute(IP, realIp);
		return true;
	}
}
