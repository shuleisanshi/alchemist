package com.yangbingdong.ccauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ybd
 * @date 19-5-27
 * @contact yangbingdong1994@gmail.com
 */
@ConfigurationProperties(prefix = CcAuthProperty.PREFIX)
@Data
public class CcAuthProperty {

	public static final String PREFIX = "cc.ccauth";

	private boolean enableJwtSession = true;

	private long localSessionExpireSecond = 5 * 60L;

	private long localSessionCacheMaximumSize = 50_000;

}
