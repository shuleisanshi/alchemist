package com.youngbingdong.ccredisoper.extend.dlock;

import com.youngbingdong.ccredisoper.extend.dlock.exception.AcquireTimeOutException;

/**
 * @author ybd
 * @date 19-5-16
 * @contact yangbingdong1994@gmail.com
 */
public interface AfterAcquireCommand<T> {
	T executeCommand() throws Throwable;

	default T fallback() {
		throw new AcquireTimeOutException();
	}
}
