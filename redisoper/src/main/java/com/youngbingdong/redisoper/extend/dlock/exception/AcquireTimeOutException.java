package com.youngbingdong.redisoper.extend.dlock.exception;

/**
 * @author ybd
 * @date 18-8-1
 * @contact yangbingdong1994@gmail.com
 */
public class AcquireTimeOutException extends RuntimeException {
	public AcquireTimeOutException() {
		super();
	}

	public AcquireTimeOutException(String message) {
		super(message);
	}

	public AcquireTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}

	public AcquireTimeOutException(Throwable cause) {
		super(cause);
	}

	protected AcquireTimeOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	private static final long serialVersionUID = -7700971574655725988L;
}
