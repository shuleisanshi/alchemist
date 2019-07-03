package com.yangbingdong.ccmvc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ybd
 * @date 19-5-24
 * @contact yangbingdong1994@gmail.com
 */
@Data
@ConfigurationProperties(CcMvcProperty.PREFIX)
public class CcMvcProperty {
	public static final String PREFIX = "cc.ccmvc";

	private boolean oplog;

	private Disruptor disruptor = new Disruptor();


	@Data
	public static class Disruptor {
		private int power = 10;

		private int worker = 2;
	}

}
