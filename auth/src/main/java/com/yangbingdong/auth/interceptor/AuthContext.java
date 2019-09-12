package com.yangbingdong.auth.interceptor;

import com.youngbingdong.util.jwt.Jwt;
import com.youngbingdong.util.jwt.JwtPayload;
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
public class AuthContext<T extends JwtPayload<T>> {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Method method;
    private Jwt<T> jwt;

    public static <T extends JwtPayload<T>> AuthContext<T> of(HttpServletRequest request, HttpServletResponse response, Method method, Jwt<T> jwt) {
        AuthContext<T> authContext = new AuthContext<>();
        authContext.setRequest(request)
                   .setResponse(response)
                   .setMethod(method)
                   .setJwt(jwt);
        return authContext;
    }
}
