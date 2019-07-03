package com.yangbingdong.ccmvc.config;

import com.yangbingdong.ccmvc.disruptor.DisruptorEngine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
@EnableConfigurationProperties(CcMvcProperty.class)
public class DisruptorAutoConfiguration {

	@Bean
	public DisruptorEngine disruptorEngine(CcMvcProperty ccMvcProperty) {
		return new DisruptorEngine(ccMvcProperty.getDisruptor());
	}
}
