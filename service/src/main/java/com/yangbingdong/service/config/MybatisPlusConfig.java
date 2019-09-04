package com.yangbingdong.service.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.yangbingdong.service.injector.CustomLogicSqlInjector;
import com.yangbingdong.service.injector.CustomMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author miemie
 * @since 2018-08-12
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public CustomLogicSqlInjector customLogicSqlInjector() {
        return new CustomLogicSqlInjector();
    }

    @Bean
    public CustomMetaObjectHandler customMetaObjectHandler() {
        return new CustomMetaObjectHandler();
    }
}
