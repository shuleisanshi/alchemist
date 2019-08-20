package com.yangbingdong.auth.interceptor.handler;

import com.yangbingdong.auth.config.AuthProperty;
import com.yangbingdong.auth.interceptor.AuthContext;
import com.yangbingdong.auth.jwt.JwtOperator;
import com.youngbingdong.redisoper.extend.commom.CommonRedisoper;
import com.youngbingdong.util.time.SystemTimer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.yangbingdong.auth.AuthorizeConstant.AUTHORIZATION_HEADER;
import static com.yangbingdong.auth.AuthorizeConstant.REFRESH_INTERVAL;
import static com.yangbingdong.auth.AuthorizeConstant.REFRESH_TOKEN_LOCK_PREFIX;
import static com.youngbingdong.util.jwt.JwtUtils.assertTrue;
import static com.youngbingdong.util.jwt.JwtUtils.parseJwt;
import static com.youngbingdong.util.jwt.JwtUtils.validTokenPrefix;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author ybd
 * @date 19-5-22
 * @contact yangbingdong1994@gmail.com
 */
public abstract class AbstractJwtAuthorizationPreHandler implements AuthorizationPreHandler {

	@Resource
	private CommonRedisoper commonRedisoper;

	@Resource
	private JwtOperator jwtOperator;

	@Resource
	private AuthProperty authProperty;

	protected abstract void preHandler(AuthContext authContext);

	@Override
	public void preHandleAuth(HttpServletRequest request, HttpServletResponse response, Method method) {
		Jws<Claims> claimsJws = checkAndGetJwtFromAuthHeader(request);
		checkSessionExpire(claimsJws);
		preHandler(AuthContext.of(request, response, method, claimsJws));
		refreshJwtIfProximityTimeout(response, claimsJws);
	}

	private Jws<Claims> checkAndGetJwtFromAuthHeader(HttpServletRequest request) {
		String authorizationToken = request.getHeader(AUTHORIZATION_HEADER);
		assertTrue(validTokenPrefix(authorizationToken), "Invalid Token Prefix");
		return parseJwt(authorizationToken);
	}

	private void checkSessionExpire(Jws<Claims> claimsJws) {
		if (authProperty.isEnableJwtSession()) {
			String sessionExpKey = jwtOperator.getSessionExpKey(claimsJws.getSignature());
			Long ttl = jwtOperator.getSessionTtlCache().get(sessionExpKey, key -> commonRedisoper.ttl(key));
			assertTrue(ttl != null && ttl > 0, "会话已过期");
		}
	}

	private void refreshJwtIfProximityTimeout(HttpServletResponse response, Jws<Claims> claimsJws) {
		Claims claims = claimsJws.getBody();
		long interval = claims.getExpiration().getTime() - SystemTimer.now();
		if (interval < REFRESH_INTERVAL) {
			Boolean nx = commonRedisoper.setNx(REFRESH_TOKEN_LOCK_PREFIX + claimsJws.getSignature(), EMPTY, 5L);
			if (nx) {
				jwtOperator.grantJwt(claims.getSubject(), response);
			}
		}
	}
}
