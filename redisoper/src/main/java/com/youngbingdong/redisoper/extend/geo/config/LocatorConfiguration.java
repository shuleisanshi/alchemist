package com.youngbingdong.redisoper.extend.geo.config;

import com.youngbingdong.redisoper.extend.geo.locate.GaodeLocator;
import com.youngbingdong.redisoper.extend.geo.locate.Locator;
import com.youngbingdong.redisoper.extend.geo.locate.TencentLocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.youngbingdong.redisoper.extend.geo.config.LocatorEnum.TENCENT;

/**
 * @author ybd
 * @date 2019/8/16
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@EnableConfigurationProperties(LocatorProperty.class)
public class LocatorConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "alchemist.redisoper.locator", name = "enable", havingValue = "true")
    public Locator locator(LocatorProperty locatorProperty) {
        return TENCENT.equals(locatorProperty.getType()) ? new TencentLocator(locatorProperty) : new GaodeLocator(locatorProperty);
    }
}
