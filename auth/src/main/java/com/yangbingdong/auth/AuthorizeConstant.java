package com.yangbingdong.auth;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
public interface AuthorizeConstant {

    String SESSION_EXP_KEY_PREFIX = "SESSION:";

	long DEFAULT_REFRESH_INTERVAL_MILLI = 60 * 60 * 1000;

	long DEFAULT_SESSION_EXPIRATION_SECOND = 24 * 60 * 60;

    String REFRESH_TOKEN_LOCK_PREFIX = "RefreshTokenLock:";
}
