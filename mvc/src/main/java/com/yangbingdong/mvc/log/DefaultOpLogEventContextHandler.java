package com.yangbingdong.mvc.log;

import com.yangbingdong.mvc.log.core.OpLogEventSourceHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class DefaultOpLogEventContextHandler implements OpLogEventSourceHandler<OpLogContext> {

	@Override
	public void handlerSource(OpLogContext source) {
		source.parseOperatingSystemAndBrowser()
			  .parseReceiveData();
		log.info("opLog: {}", source);
	}

}
