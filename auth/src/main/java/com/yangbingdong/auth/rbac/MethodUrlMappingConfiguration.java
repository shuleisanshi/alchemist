package com.yangbingdong.auth.rbac;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ybd
 * @date 2019/9/17
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class MethodUrlMappingConfiguration {

    @ConditionalOnProperty(prefix = "alchemist.auth", name = "register-method-url-mapping", havingValue = "true")
    @Bean
    public MethodUrlMapping methodUrlRegister() {
        return new MethodUrlMapping();
    }
}
