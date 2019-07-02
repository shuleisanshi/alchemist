package com.yanglaoban.ccmvc.log;

import com.yanglaoban.ccmvc.disruptor.DisruptorEventSourceHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class DefaultOpLogEventContextHandler implements DisruptorEventSourceHandler<OpLogContext> {

	@Override
	public void handlerSource(OpLogContext source) {
		source.parseOperatingSystemAndBrowser()
			  .parseReceiveData();
		log.info("opLog: {}", source);
	}

}
