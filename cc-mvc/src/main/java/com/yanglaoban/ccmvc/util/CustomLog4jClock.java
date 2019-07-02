package com.yanglaoban.ccmvc.util;

import com.youngboss.ccutil.time.SystemTimer;
import org.apache.logging.log4j.core.util.Clock;

/**
 * @author ybd
 * @date 19-5-30
 * @contact yangbingdong1994@gmail.com
 */
public class CustomLog4jClock implements Clock {
	@Override
	public long currentTimeMillis() {
		return SystemTimer.now();
	}
}
