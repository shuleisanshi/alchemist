package com.youngboss.ccutil.jwt;

import com.youngboss.ccutil.time.SystemTimer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Deserializer;
import io.jsonwebtoken.io.Serializer;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author ybd
 * @date 19-4-9
 * @contact yangbingdong1994@gmail.com
 */
public class JwtUtils {

	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	private static final Key SECRET_KEY = new SecretKeySpec("PRIVATE_ccec6faa-ad93-45f7-ac8e-1feb148dce92".getBytes(), SIGNATURE_ALGORITHM.getJcaName());
	private static final Serializer<Map<String, ?>> SERIALIZER = new FastJwtSerializer();
	private static final Deserializer<Map<String, ?>> DESERIALIZER = new FastJwtDeserializer();
	private static final String BEARER = "Bearer ";
	private static final int TOKEN_PREFIX_LENGTH = BEARER.length();


	public static String genJwt(String sub, long ttlMillis) {
		JwtBuilder jwtBuilder = Jwts.builder()
									.setSubject(sub)
									.serializeToJsonWith(SERIALIZER)
									.signWith(SECRET_KEY, SIGNATURE_ALGORITHM);
		if (ttlMillis > 0) {
			jwtBuilder.setExpiration(new Date(SystemTimer.now() + ttlMillis));
		}
		return jwtBuilder.compact();
	}

	public static String genJwtTokenHeader(String jwt) {
		return BEARER + jwt;
	}

	public static boolean validTokenPrefix(String token) {
		return token != null && token.startsWith(BEARER);
	}

	public static Jws<Claims> parseJwt(String token) {
		try {
			return Jwts.parser()
					   .deserializeJsonWith(DESERIALIZER)
					   .setSigningKey(SECRET_KEY)
					   .setAllowedClockSkewSeconds(10)
					   .parseClaimsJws(token.substring(TOKEN_PREFIX_LENGTH));
		} catch (ExpiredJwtException e) {
			throw new TokenException("Token已过期");
		} catch (UnsupportedJwtException e) {
			throw new TokenException("Token格式错误");
		} catch (MalformedJwtException e) {
			throw new TokenException("Token没有被正确构造");
		} catch (SignatureException e) {
			throw new TokenException("Token签名失败");
		} catch (IllegalArgumentException e) {
			throw new TokenException("非法参数");
		}
	}

	public static String getSignatureFromJwtString(String jwt) {
		return jwt.split("\\.")[2];
	}

	public static void assertTrue(boolean result, String message) {
		if (!result) {
			throw new TokenException(message);
		}
	}

}
