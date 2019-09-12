package com.yangbingdong.auth;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
public interface AuthorizeConstant {

    String SESSION_EXP_KEY_PREFIX = "SESSION:";

	long REFRESH_INTERVAL_MILLI = 60 * 60 * 1000;

	long SESSION_EXPIRATION_SECOND = 24 * 60 * 60;

	long SESSION_EXPIRATION_MILLI = SESSION_EXPIRATION_SECOND * 1000;

	String REFRESH_TOKEN_LOCK_PREFIX = "RefreshTokenLock:";
}
