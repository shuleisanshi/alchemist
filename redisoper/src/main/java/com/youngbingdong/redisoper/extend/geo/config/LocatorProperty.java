package com.youngbingdong.redisoper.extend.geo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ybd
 * @date 2019/8/16
 * @contact yangbingdong1994@gmail.com
 */
@Data
@ConfigurationProperties(prefix = "alchemist.redisoper.locator")
public class LocatorProperty {
    private LocatorEnum type = LocatorEnum.TENCENT;

    private Boolean enable = false;

    private String key;

    private String sign;
}

