package com.yangbingdong.mvc.mvcconfigurer;

import com.yangbingdong.mvc.interceptor.IpInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

/**
 * @author ybd
 * @date 18-3-7
 * @contact yangbingdong1994@gmail.com
 */
@RequiredArgsConstructor
public class IpInterceptorMvcConfigurer implements WebMvcConfigurer {

	private final IpInterceptor ipInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (Objects.nonNull(ipInterceptor)) {
			registry.addInterceptor(ipInterceptor);
		}
	}
}
