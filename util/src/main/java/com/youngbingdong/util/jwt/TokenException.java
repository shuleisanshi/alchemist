package com.youngbingdong.util.jwt;

/**
 * @author ybd
 * @date 19-4-9
 * @contact yangbingdong1994@gmail.com
 */
public class TokenException extends RuntimeException{
	private static final long serialVersionUID = -7706921597357928L;

	public TokenException() {
		super();
	}

	public TokenException(String message) {
		super(message);
	}

	public TokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenException(Throwable cause) {
		super(cause);
	}

	protected TokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
