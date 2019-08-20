package com.yangbingdong.mvc.config;

import com.yangbingdong.mvc.disruptor.DisruptorEngine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
@EnableConfigurationProperties(MvcProperty.class)
public class DisruptorAutoConfiguration {

	@Bean
	public DisruptorEngine disruptorEngine(MvcProperty mvcProperty) {
		return new DisruptorEngine(mvcProperty.getDisruptor());
	}
}
