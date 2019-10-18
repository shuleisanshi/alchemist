package com.yangbingdong.auth.interceptor.handler;

import com.alibaba.fastjson.util.TypeUtils;
import com.yangbingdong.auth.config.AuthProperty;
import com.yangbingdong.auth.interceptor.AuthContext;
import com.yangbingdong.auth.jwt.JwtOperator;
import com.youngbingdong.redisoper.extend.commom.CommonRedisoper;
import com.youngbingdong.util.jwt.Jwt;
import com.youngbingdong.util.jwt.JwtPayload;
import com.youngbingdong.util.jwt.TokenException;
import com.youngbingdong.util.time.SystemTimer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static cn.hutool.core.util.StrUtil.EMPTY;
import static cn.hutool.core.util.StrUtil.isEmpty;
import static com.yangbingdong.auth.AuthorizeConstant.REFRESH_TOKEN_LOCK_PREFIX;
import static com.youngbingdong.util.jwt.AuthUtil.AUTHORIZATION_HEADER;
import static com.youngbingdong.util.jwt.AuthUtil.parseAuthJwt;
import static com.youngbingdong.util.jwt.AuthUtil.validTokenPrefix;

/**
 * @author ybd
 * @date 19-5-22
 * @contact yangbingdong1994@gmail.com
 */
public abstract class AbstractJwtAuthorizationPreHandler<T extends JwtPayload<T>> implements AuthorizationPreHandler {

	@Resource
	private CommonRedisoper commonRedisoper;

	@Resource
	private JwtOperator jwtOperator;

	@Resource
	private AuthProperty authProperty;

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    public AbstractJwtAuthorizationPreHandler() {
        Type genericParamType = TypeUtils.getGenericParamType(this.getClass());
        this.clazz = (Class<T>) ((ParameterizedType) genericParamType).getActualTypeArguments()[0];
    }

    protected abstract void preHandle(AuthContext<T> authContext);

	@Override
	public void preHandleAuth(HttpServletRequest request, HttpServletResponse response, Method method) {
        preHandle(request, response, method);
	}

	protected void preHandle(HttpServletRequest request, HttpServletResponse response, Method method){
        Jwt<T> jwt = checkAndGetJwtFromAuthHeader(request);
        checkSessionExpire(jwt);
        preHandle(AuthContext.of(request, response, method, jwt));
        refreshJwtIfProximityTimeout(response, jwt);
    }


	private Jwt<T> checkAndGetJwtFromAuthHeader(HttpServletRequest request) {
		String authorizationToken = request.getHeader(AUTHORIZATION_HEADER);
		if(isEmpty(authorizationToken)) {
            throw new TokenException("Token could not be null");
        }
        if (!validTokenPrefix(authorizationToken)) {
            throw new TokenException("Invalid Token Prefix");
        }
		return parseAuthJwt(authorizationToken, authProperty.getSignKey(), clazz);
	}

	private void checkSessionExpire(Jwt<? extends JwtPayload> jwt) {
		if (authProperty.isEnableJwtSession()) {
			String sessionExpKey = jwtOperator.getSessionExpKey(jwt.getSign());
			Long ttl = jwtOperator.getSessionTtlCache().get(sessionExpKey, key -> commonRedisoper.ttl(key));
            if (ttl == null || ttl <= 0) {
                throw new TokenException("Invalid Token");
            }
		}
	}

	private void refreshJwtIfProximityTimeout(HttpServletResponse response, Jwt<? extends JwtPayload> jwt) {
        long expire = jwt.getJwtHeader().getExpire();
        long interval = expire - SystemTimer.now();
		if (interval < authProperty.getRefreshIntervalMilli()) {
			Boolean nx = commonRedisoper.setNx(REFRESH_TOKEN_LOCK_PREFIX + jwt.getSign(), EMPTY, 600L);
			if (nx) {
				jwtOperator.grantAuthJwt(jwt.getPayload(), response);
			}
		}
	}
}
