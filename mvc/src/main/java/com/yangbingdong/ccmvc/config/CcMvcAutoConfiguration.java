package com.yangbingdong.ccmvc.config;

import com.yangbingdong.ccmvc.filter.MDCCleanFilter;
import com.yangbingdong.ccmvc.interceptor.IpInterceptor;
import com.yangbingdong.ccmvc.mvcconfigurer.FastJsonConverterWebMvcConfigurer;
import com.yangbingdong.ccmvc.mvcconfigurer.IpInterceptorMvcConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author ybd
 * @date 19-5-6
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class CcMvcAutoConfiguration {

	@Bean
	public FastJsonConverterWebMvcConfigurer customWebMvcConfiguration() {
		return new FastJsonConverterWebMvcConfigurer();
	}

	@Order(0)
	@Bean
	public IpInterceptorMvcConfigurer ipInterceptorMvcConfigurer() {
		return new IpInterceptorMvcConfigurer(new IpInterceptor());
	}

	@Bean
	public FilterRegistrationBean<MDCCleanFilter> mdcCleanFilterFilterRegistrationBean() {
		FilterRegistrationBean<MDCCleanFilter> registrationBean = new FilterRegistrationBean<>();
		MDCCleanFilter mdcCleanFilter = new MDCCleanFilter();
		registrationBean.setFilter(mdcCleanFilter);
		registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registrationBean;
	}


}
