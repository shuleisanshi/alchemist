package com.yangbingdong.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.yangbingdong.auth.AuthorizeConstant.DEFAULT_REFRESH_INTERVAL_MILLI;
import static com.yangbingdong.auth.AuthorizeConstant.DEFAULT_SESSION_EXPIRATION_SECOND;

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

    private long sessionExpireSecond = DEFAULT_SESSION_EXPIRATION_SECOND;

	private long localSessionExpireSecond = 5 * 60L;

	private long localSessionCacheMaximumSize = 50_000;

    private long refreshIntervalMilli = DEFAULT_REFRESH_INTERVAL_MILLI;

    private boolean registerMethodUrlMapping = false;

}
