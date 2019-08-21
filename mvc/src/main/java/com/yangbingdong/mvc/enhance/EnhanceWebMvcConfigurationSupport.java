package com.yangbingdong.mvc.enhance;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author ybd
 * @date 2019/8/21
 * @contact yangbingdong1994@gmail.com
 */
@Configuration(proxyBeanMethods = false)
public class EnhanceWebMvcConfigurationSupport extends WebMvcConfigurationSupport {

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new EnhanceRequestMappingHandlerMapping();
    }
}
