package com.yangbingdong.mvc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ybd
 * @date 19-5-24
 * @contact yangbingdong1994@gmail.com
 */
@Data
@ConfigurationProperties(MvcProperty.PREFIX)
public class MvcProperty {
	public static final String PREFIX = "alchemist.mvc";

	private boolean opLog;

	private boolean printRequestInfoIfError;

	private boolean printRequestInfoIfBusinessError;

	private Disruptor disruptor = new Disruptor();


	@Data
	public static class Disruptor {
		private int power = 10;

		private int worker = 2;
	}

}
