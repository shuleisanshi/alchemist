package com.youngbingdong.util.jwt;

import javax.servlet.http.HttpServletRequest;

import static cn.hutool.core.util.StrUtil.EMPTY;
import static cn.hutool.core.util.StrUtil.isEmpty;
import static com.youngbingdong.util.spring.RequestHolder.currentRequest;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
public class AuthUtil {

    public static final String BEARER = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final int BEARER_LENGTH = BEARER.length();

    public static boolean validTokenPrefix(String token) {
        return !token.startsWith(BEARER);
    }

    public static String grantAuthJwt(JwtPayload jwtPayload, String key, long ttlMillis) {
        if (ttlMillis <= 0) {
            throw new IllegalArgumentException("ttl must grater then 0");
        }
        return BEARER + JwtUtil.grantJwt(jwtPayload, key, ttlMillis);
    }

    public static String getSignFromAuthJwt(String token) {
        int index = token.lastIndexOf(".");
        if (index != -1) {
            return token.substring(index + 1);
        }
        return EMPTY;
    }

    public static String getSignatureFromCurrentReq() {
        HttpServletRequest request = currentRequest();
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (isEmpty(token)) {
            return EMPTY;
        }
        return getSignFromAuthJwt(token);
    }

    public static <T extends JwtPayload> Jwt<T> parseAuthJwt(String jwtStr, String key, Class<T> clazz) {
        return JwtUtil.parseJwt(jwtStr.substring(BEARER_LENGTH), key, clazz);
    }
}
