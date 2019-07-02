package com.yanglaoban.ccmvc.disruptor;


import com.lmax.disruptor.WorkHandler;

import java.util.Map;

import static org.springframework.util.Assert.notNull;


/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
class DisruptorInnerShardingHandler<S> implements WorkHandler<DisruptorEvent<S>> {

	private Map<Class, DisruptorEventSourceHandler<S>> handlerMap;

	public DisruptorInnerShardingHandler(Map<Class, DisruptorEventSourceHandler<S>> handlerMap) {
		this.handlerMap = handlerMap;
	}

	@Override
	public void onEvent(DisruptorEvent<S> event) throws Exception {
		try {
			Class sourceClass = event.getSourceClass();
			DisruptorEventSourceHandler<S> sourceHandler = handlerMap.get(sourceClass);
			notNull(sourceHandler, "Source handler not found: " + sourceClass);
			sourceHandler.handlerSource(event.getSource());
		} finally {
			event.clean();
		}
	}
}
