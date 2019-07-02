package com.yanglaoban.ccmvc.annotated;

import com.yanglaoban.ccmvc.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author ybd
 * @date 18-5-15
 * @contact yangbingdong1994@gmail.com
 */
@RestControllerAdvice(annotations = Rest.class)
public class GlobalControllerAdvisor implements ResponseBodyAdvice {
	private static final String VOID = "void";

	/**
	 * String 类型不支持
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return !(returnType.getGenericParameterType() instanceof Class) || !((Class<?>) returnType.getGenericParameterType()).isAssignableFrom(String.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		return isVoidMethod(returnType) ? Response.ok() : Response.ok(body);
	}

	private boolean isVoidMethod(MethodParameter returnType) {
		return VOID.equals(returnType.getGenericParameterType().getTypeName());
	}
}
