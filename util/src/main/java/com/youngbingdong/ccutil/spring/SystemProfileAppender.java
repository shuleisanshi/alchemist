package com.youngbingdong.ccutil.spring;

import org.apache.commons.lang3.StringUtils;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.INCLUDE_PROFILES_PROPERTY;

/**
 * @author ybd
 * @date 19-5-20
 * @contact yangbingdong1994@gmail.com
 */
public class SystemProfileAppender {

	public static void appendProfile(String profile) {
		String property = System.getProperty(INCLUDE_PROFILES_PROPERTY);
		if (StringUtils.isBlank(property)) {
			property = profile;
		} else {
			property = property + "," + profile;
		}
		System.setProperty(INCLUDE_PROFILES_PROPERTY, property);
	}
}
