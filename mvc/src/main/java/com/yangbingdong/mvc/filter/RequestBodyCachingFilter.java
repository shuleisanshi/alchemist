package com.yangbingdong.mvc.filter;

import com.youngbingdong.util.spring.RequestBodyCachingWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ybd
 * @date 2019/9/11
 * @contact yangbingdong1994@gmail.com
 */
public class RequestBodyCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        if (!"GET".equals(method) && !"OPTIONS".equals(method)) {
            filterChain.doFilter(new RequestBodyCachingWrapper(request), response);
        }
    }

}
