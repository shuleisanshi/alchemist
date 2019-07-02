package com.youngboss.ccutil.spring;

import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

/**
 * @author ybd
 * @date 19-4-9
 * @contact yangbingdong1994@gmail.com
 */
public class RequestHolder {
	@SuppressWarnings("ConstantConditions")
	public static HttpServletResponse currentResponse() {
		return ((ServletRequestAttributes) getRequestAttributes()).getResponse();
	}

	@SuppressWarnings("ConstantConditions")
	public static HttpServletRequest currentRequest() {
		return ((ServletRequestAttributes) getRequestAttributes()).getRequest();
	}
}
