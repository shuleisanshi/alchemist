package com.yangbingdong.mvc.log.core;

import com.yangbingdong.mvc.disruptor.DisruptorEventSourceHandler;
import com.yangbingdong.mvc.log.OpLogContext;

/**
 * @author ybd
 * @date 2019/9/10
 * @contact yangbingdong1994@gmail.com
 */
public interface OpLogEventSourceHandler<T extends OpLogContext> extends DisruptorEventSourceHandler<T> {
}
