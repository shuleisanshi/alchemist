package com.yangbingdong.mvc.disruptor;


import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
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
            if (sourceHandler == null) {
                log.error("Source handler not found: " + sourceClass);
            } else {
                sourceHandler.handlerSource(event.getSource());
            }
		} finally {
			event.clean();
		}
	}
}
