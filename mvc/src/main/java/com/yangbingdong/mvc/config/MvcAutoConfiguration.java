package com.yangbingdong.mvc.config;

import com.yangbingdong.mvc.enhance.EnhanceWebMvcConfigurationSupport;
import com.yangbingdong.mvc.filter.MDCCleanFilter;
import com.yangbingdong.mvc.filter.RequestBodyCachingFilter;
import com.yangbingdong.mvc.interceptor.IpInterceptor;
import com.yangbingdong.mvc.mvcconfigurer.FastJsonConverterWebMvcConfigurer;
import com.yangbingdong.mvc.mvcconfigurer.IpInterceptorMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author ybd
 * @date 19-5-6
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class MvcAutoConfiguration {

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

    @Conditional(RequestBodyCachingCondition.class)
    @Bean
    public FilterRegistrationBean<RequestBodyCachingFilter> requestBodyCachingFilterFilterRegistrationBean() {
        FilterRegistrationBean<RequestBodyCachingFilter> registrationBean = new FilterRegistrationBean<>();
        RequestBodyCachingFilter requestBodyCachingFilter = new RequestBodyCachingFilter();
        registrationBean.setFilter(requestBodyCachingFilter);
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 8);
        return registrationBean;
    }

    @Bean
    public WebMvcConfigurationSupport webMvcConfigurationSupport() {
        return new EnhanceWebMvcConfigurationSupport();
    }

    private static class RequestBodyCachingCondition extends AnyNestedCondition {

        public RequestBodyCachingCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(name = "alchemist.mvc.print-request-info-if-error", havingValue = "true")
        static class GlobalCondition {

        }

        @ConditionalOnProperty(name = "alchemist.mvc.print-request-info-if-business-error", havingValue = "true")
        static class BusinessCondition {

        }
    }

}
