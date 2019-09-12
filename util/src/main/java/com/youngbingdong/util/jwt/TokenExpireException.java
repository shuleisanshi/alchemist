package com.youngbingdong.util.jwt;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
public class TokenExpireException extends RuntimeException {

    private static final long serialVersionUID = -4277887524692232649L;

    public TokenExpireException() {
        super();
    }

    public TokenExpireException(String message) {
        super(message);
    }

    public TokenExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpireException(Throwable cause) {
        super(cause);
    }

    protected TokenExpireException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
