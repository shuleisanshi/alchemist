package com.yangbingdong.ccauth.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ybd
 * @date 19-5-17
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class AuthContext {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Method method;
	private Jws<Claims> claimsJws;

	public static AuthContext of(HttpServletRequest request, HttpServletResponse response, Method method, Jws<Claims> claimsJws) {
		return new AuthContext().setRequest(request)
								.setResponse(response)
								.setMethod(method)
								.setClaimsJws(claimsJws);
	}

	public String getSubject() {
		return this.claimsJws.getBody().getSubject();
	}
}
