package com.yangbingdong.ccmvc.config;

import com.yangbingdong.ccmvc.disruptor.DisruptorEventSourceHandler;
import com.yangbingdong.ccmvc.log.core.OpLogAspect;
import com.yangbingdong.ccmvc.log.core.OpLogContextBuilder;
import com.yangbingdong.ccmvc.log.DefaultOpLogContextBuilder;
import com.yangbingdong.ccmvc.log.DefaultOpLogEventContextHandler;
import com.yangbingdong.ccmvc.log.OpLogContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@ConditionalOnProperty(prefix = OpLogConfiguration.PREFIX, name = "oplog", havingValue = "true")
@Import(DisruptorAutoConfiguration.class)
public class OpLogConfiguration {

	public static final String PREFIX = "cc.ccmvc";

	public static final String OP_LOG_EVENT_CONTEXT_HANDLER = "opLogEventContextHandler";

	@Bean
	@ConditionalOnMissingBean
	public OpLogContextBuilder<? extends OpLogContext> opLogContextBuilder() {
		return new DefaultOpLogContextBuilder();
	}

	@Bean(name = OP_LOG_EVENT_CONTEXT_HANDLER)
	@ConditionalOnMissingBean(name = OP_LOG_EVENT_CONTEXT_HANDLER)
	public DisruptorEventSourceHandler<? extends OpLogContext> opLogEventContextHandler() {
		return new DefaultOpLogEventContextHandler();
	}

	@Bean
	public OpLogAspect opLogAspect() {
		return new OpLogAspect();
	}
}
