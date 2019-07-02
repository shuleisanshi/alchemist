package com.yanglaoban.ccauth;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
public interface AuthorizeConstant {

	String BEARER = "Bearer ";

	int TOKEN_PREFIX_LENGTH = BEARER.length();

	String AUTHORIZATION_HEADER = "Authorization";

	String SESSION_EXP_KEY_PREFIX = "SESSION:";

	long REFRESH_INTERVAL = 30 * 60 * 1000;

	long SESSION_EXPIRATION_SECOND = 2 * 60 * 60;

	long SESSION_EXPIRATION_MILLI = SESSION_EXPIRATION_SECOND * 1000;

	String REFRESH_TOKEN_LOCK_PREFIX = "RefreshTokenLock:";
}
