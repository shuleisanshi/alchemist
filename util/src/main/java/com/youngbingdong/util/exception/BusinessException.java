package com.youngbingdong.util.exception;

/**
 * 业务异常
 * @author qww
 */
public class BusinessException extends RuntimeException {


	private static final long serialVersionUID = 8719694009967156978L;

	public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
