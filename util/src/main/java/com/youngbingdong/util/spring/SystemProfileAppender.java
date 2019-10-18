package com.youngbingdong.util.spring;

import cn.hutool.core.util.StrUtil;

import static org.springframework.boot.context.config.ConfigFileApplicationListener.INCLUDE_PROFILES_PROPERTY;

/**
 * @author ybd
 * @date 19-5-20
 * @contact yangbingdong1994@gmail.com
 */
public class SystemProfileAppender {

	public static void appendProfile(String profile) {
		String property = System.getProperty(INCLUDE_PROFILES_PROPERTY);
		if (StrUtil.isBlank(property)) {
			property = profile;
		} else {
			property = property + "," + profile;
		}
		System.setProperty(INCLUDE_PROFILES_PROPERTY, property);
	}
}
