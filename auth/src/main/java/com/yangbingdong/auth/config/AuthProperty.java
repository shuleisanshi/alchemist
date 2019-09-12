package com.yangbingdong.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ybd
 * @date 19-5-27
 * @contact yangbingdong1994@gmail.com
 */
@ConfigurationProperties(prefix = AuthProperty.PREFIX)
@Data
public class AuthProperty {

	public static final String PREFIX = "alchemist.auth";

	private boolean enableJwtSession = true;

    private String signKey = "DEFAULT_SIGN_KEY";

	private long localSessionExpireSecond = 5 * 60L;

	private long localSessionCacheMaximumSize = 50_000;

}
