package com.yangbingdong.auth.jwt;

import com.github.benmanes.caffeine.cache.Cache;
import com.yangbingdong.auth.config.AuthProperty;
import com.youngbingdong.redisoper.extend.commom.CommonRedisoper;
import lombok.Getter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.yangbingdong.auth.AuthorizeConstant.AUTHORIZATION_HEADER;
import static com.yangbingdong.auth.AuthorizeConstant.SESSION_EXPIRATION_MILLI;
import static com.yangbingdong.auth.AuthorizeConstant.SESSION_EXPIRATION_SECOND;
import static com.yangbingdong.auth.AuthorizeConstant.SESSION_EXP_KEY_PREFIX;
import static com.yangbingdong.auth.AuthorizeConstant.TOKEN_PREFIX_LENGTH;
import static com.youngbingdong.util.jwt.JwtUtils.genJwt;
import static com.youngbingdong.util.jwt.JwtUtils.genJwtTokenHeader;
import static com.youngbingdong.util.jwt.JwtUtils.getSignatureFromJwtString;
import static com.youngbingdong.util.spring.RequestHolder.currentRequest;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author ybd
 * @date 19-4-20
 * @contact yangbingdong1994@gmail.com
 */
public class JwtOperator {

	@Getter
	private Cache<String, Long> sessionTtlCache;

	@Resource
	private CommonRedisoper commonRedisoper;

	@Resource
	private AuthProperty authProperty;

	public JwtOperator(Cache<String, Long> sessionTtlCache) {
		this.sessionTtlCache = sessionTtlCache;
	}

	public void grantJwt(String sub, HttpServletResponse httpServletResponse) {
		String jwt = genJwt(sub, SESSION_EXPIRATION_MILLI);
		String tokenHeader = genJwtTokenHeader(jwt);
		httpServletResponse.setHeader(AUTHORIZATION_HEADER, tokenHeader);
		if (authProperty.isEnableJwtSession()) {
			String sessionExpKey = getSessionExpKey(getSignatureFromJwtString(jwt));
			commonRedisoper.set(sessionExpKey, EMPTY, SESSION_EXPIRATION_SECOND);
			sessionTtlCache.put(sessionExpKey, SESSION_EXPIRATION_SECOND);
		}
	}

	public String getSessionExpKey(String signature) {
		return SESSION_EXP_KEY_PREFIX + signature;
	}

	public String getSessionExpKey() {
		return SESSION_EXP_KEY_PREFIX + getSignatureFromCurrentReq();
	}

	public void eraseSession(JwtOperatorHandler jwtOperatorHandler) {
		if (!authProperty.isEnableJwtSession()) {
			return;
		}
		String sessionExpKey = getSessionExpKey();
		commonRedisoper.del(sessionExpKey);
		cleanLocalSession(sessionExpKey);
		if (jwtOperatorHandler != null) {
			jwtOperatorHandler.afterEraseSession(sessionExpKey);
		}
	}

	public String getSignatureFromCurrentReq() {
		HttpServletRequest request = currentRequest();
		String token = request.getHeader(AUTHORIZATION_HEADER);
		return getSignatureFromJwtString(token.substring(TOKEN_PREFIX_LENGTH));
	}

	public void cleanLocalSession(String sessionExpKey) {
		sessionTtlCache.invalidate(sessionExpKey);
	}
}
