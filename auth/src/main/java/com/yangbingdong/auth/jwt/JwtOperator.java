package com.yangbingdong.auth.jwt;

import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.yangbingdong.auth.config.AuthProperty;
import com.youngbingdong.redisoper.extend.commom.CommonRedisoper;
import com.youngbingdong.util.jwt.AuthUtil;
import com.youngbingdong.util.jwt.JwtPayload;
import lombok.Getter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static cn.hutool.core.util.StrUtil.EMPTY;
import static com.yangbingdong.auth.AuthorizeConstant.SESSION_EXP_KEY_PREFIX;
import static com.youngbingdong.util.jwt.AuthUtil.AUTHORIZATION_HEADER;
import static com.youngbingdong.util.jwt.AuthUtil.getSignFromAuthJwt;
import static com.youngbingdong.util.jwt.AuthUtil.getSignatureFromCurrentReq;
import static com.youngbingdong.util.spring.RequestHolder.currentResponse;

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

    public String grantAuthJwt(JwtPayload jwtPayload) {
        return grantAuthJwt(jwtPayload, currentResponse());
    }

    public String grantAuthJwt(JwtPayload jwtPayload, HttpServletResponse httpServletResponse) {
        String authJwt = AuthUtil.grantAuthJwt(jwtPayload, authProperty.getSignKey(), authProperty.getSessionExpireSecond() * 1000);
        httpServletResponse.setHeader(AUTHORIZATION_HEADER, authJwt);
        if (authProperty.isEnableJwtSession()) {
            String sessionExpKey = getSessionExpKey(getSignFromAuthJwt(authJwt));
            commonRedisoper.set(sessionExpKey, EMPTY, authProperty.getSessionExpireSecond());
            sessionTtlCache.put(sessionExpKey, authProperty.getSessionExpireSecond());
        }
        return authJwt;
    }

    public String getSessionExpKey(String signature) {
        return SESSION_EXP_KEY_PREFIX + signature;
    }

    public String getSessionExpKey() {
        return SESSION_EXP_KEY_PREFIX + getSignatureFromCurrentReq();
    }

    public void eraseSession() {
        if (!authProperty.isEnableJwtSession()) {
            return;
        }
        String sessionExpKey = getSessionExpKey();
        if (StrUtil.isEmpty(sessionExpKey)) {
            return;
        }
        commonRedisoper.del(sessionExpKey);
        cleanLocalSession(sessionExpKey);
    }

    public void cleanLocalSession(String sessionExpKey) {
        sessionTtlCache.invalidate(sessionExpKey);
    }

}
