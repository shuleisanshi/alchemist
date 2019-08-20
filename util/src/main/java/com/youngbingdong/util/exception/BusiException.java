package com.youngbingdong.util.exception;

/**
 * 业务异常
 * @author qww
 */
public class BusiException extends RuntimeException {


	private static final long serialVersionUID = 8719694009967156978L;

	public BusiException() {
    }

    public BusiException(String message) {
        super(message);
    }

    public BusiException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusiException(Throwable cause) {
        super(cause);
    }

    public BusiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
