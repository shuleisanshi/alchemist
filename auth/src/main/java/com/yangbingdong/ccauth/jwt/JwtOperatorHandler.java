package com.yangbingdong.ccauth.jwt;

/**
 * @author ybd
 * @date 19-5-21
 * @contact yangbingdong1994@gmail.com
 */
public interface JwtOperatorHandler {

	void afterEraseSession(String sessionExpKey);
}
