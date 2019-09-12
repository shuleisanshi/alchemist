package com.youngbingdong.util.jwt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.youngbingdong.util.time.SystemTimer;

import static cn.hutool.core.util.StrUtil.DOT;
import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONBytes;

/**
 * @author ybd
 * @date 2019/9/12
 * @contact yangbingdong1994@gmail.com
 */
public class JwtUtil {

    public static String grantJwt(JwtPayload jwtPayload, String key, long ttlMillis) {
        JwtHeader jwtHeader = JwtHeader.of(SystemTimer.now() + ttlMillis);
        String headerStr = Base64.encode(toJSONBytes(jwtHeader));
        String payloadStr = Base64.encode(toJSONBytes(jwtPayload));
        String sign = getSign(headerStr, payloadStr, key);
        return headerStr + DOT + payloadStr + DOT + sign;
    }

    public static <T extends JwtPayload> Jwt<T> parseJwt(String jwtStr, String key, Class<T> payloadClass) {
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;

        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwtStr.toCharArray()) {
            if (c == CharUtil.DOT) {
                CharSequence tokenSeq = clean(sb);
                String token = tokenSeq != null ? tokenSeq.toString() : null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }
                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (delimiterCount != 2) {
            throw new TokenException("JWT strings must contain exactly 2 period characters. Found: " + delimiterCount);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        if (base64UrlEncodedPayload == null) {
            throw new TokenException("JWT string '" + jwtStr + "' is missing a body/payload.");
        }
        JwtHeader header = parseObject(Base64.decode(base64UrlEncodedHeader), JwtHeader.class);
        if (header.getExpire() < SystemTimer.now()) {
            throw new TokenExpireException("JWT expired");
        }
        String sign = getSign(base64UrlEncodedHeader, base64UrlEncodedPayload, key);
        if (!sign.equals(base64UrlEncodedDigest)) {
            throw new TokenException("JWT sign fail");
        }
        Jwt<T> jwt = new Jwt<>();
        jwt.setJwtHeader(header)
           .setPayload(parseObject(Base64.decode(base64UrlEncodedPayload), payloadClass))
           .setSign(sign);
        return jwt;
    }

    private static String getSign(String headerStr, String payloadStr, String key) {
        return SecureUtil.md5(headerStr + payloadStr + key);
    }

    private static CharSequence clean(CharSequence str) {
        str = trimWhitespace(str);
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        return str;
    }

    private static CharSequence trimWhitespace(CharSequence str) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }
        final int length = str.length();

        int start = 0;
        while (start < length && Character.isWhitespace(str.charAt(start))) {
            start++;
        }

        int end = length;
        while (start < length && Character.isWhitespace(str.charAt(end - 1))) {
            end--;
        }

        return ((start > 0) || (end < length)) ? str.subSequence(start, end) : str;
    }
}
