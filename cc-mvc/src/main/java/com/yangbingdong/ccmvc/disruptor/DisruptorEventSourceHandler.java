package com.yangbingdong.ccmvc.disruptor;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
public interface DisruptorEventSourceHandler<S> {
	void handlerSource(S source) throws Exception;
}
