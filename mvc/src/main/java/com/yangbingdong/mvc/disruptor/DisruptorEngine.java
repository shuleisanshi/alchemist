package com.yangbingdong.mvc.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.yangbingdong.mvc.config.MvcProperty;
import com.youngbingdong.util.reflect.TypeUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
public class DisruptorEngine implements InitializingBean, ApplicationListener<ContextClosedEvent> {

	private final int worker;
	private int power;
	private Disruptor<DisruptorEvent> disruptor;

	@Autowired(required = false)
	private List<DisruptorEventSourceHandler> sourceHandlers = new ArrayList<>(16);
	private RingBuffer<DisruptorEvent> ringBuffer;
	private EventTranslatorOneArg<DisruptorEvent, Object> translatorOneArg;

	public DisruptorEngine(MvcProperty.Disruptor disruptorProperty) {
		this.power = disruptorProperty.getPower();
		this.worker = disruptorProperty.getWorker();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		DisruptorInnerShardingHandler[] handlerArray = buildHandlers();

		translatorOneArg = (event, sequence, arg0) -> event.setSource(arg0);
		disruptor = DisruptorUtil.createDisruptor(DisruptorEvent::new, 1 << power);
		disruptor.handleEventsWithWorkerPool(handlerArray);
		disruptor.start();
		ringBuffer = disruptor.getRingBuffer();
	}

	@SuppressWarnings("unchecked")
	private DisruptorInnerShardingHandler[] buildHandlers() {
		Map<Class, DisruptorEventSourceHandler> handlerMap = new HashMap<>(sourceHandlers.size());
		for (DisruptorEventSourceHandler sourceHandler : sourceHandlers) {
			Class clazz = TypeUtil.getClassFromGenericTypeInterface(sourceHandler, DisruptorEventSourceHandler.class);
			handlerMap.put(clazz, sourceHandler);
		}
		return IntStream.range(0, worker)
						.mapToObj(i -> new DisruptorInnerShardingHandler(handlerMap))
						.toArray(DisruptorInnerShardingHandler[]::new);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		DisruptorUtil.shutDownDisruptor(disruptor);
	}

	public void publish(Object eventSource) {
		ringBuffer.publishEvent(translatorOneArg, eventSource);
	}
}
