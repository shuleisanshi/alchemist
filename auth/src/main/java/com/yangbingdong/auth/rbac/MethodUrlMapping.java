package com.yangbingdong.auth.rbac;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cn.hutool.core.util.CharUtil.DELIM_END;
import static cn.hutool.core.util.CharUtil.DELIM_START;

/**
 * @author ybd
 * @date 2019/9/17
 * @contact yangbingdong1994@gmail.com
 */
public class MethodUrlMapping implements InitializingBean {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private Map<Method, String> methodUrlMap;

    @Override
    public void afterPropertiesSet() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        methodUrlMap = new HashMap<>(handlerMethods.size());
        handlerMethods.forEach((k, v) -> {
            Set<String> patterns = k.getPatternsCondition().getPatterns();
            String path = new ArrayList<>(patterns).get(0);
            methodUrlMap.put(v.getMethod(), formatPatternUrl(path));
        });
    }

    private String formatPatternUrl(String url) {
        char[] chars = url.toCharArray();
        int start = 0;
        int end = 0;
        int i = 0;
        for (char c : chars) {
            if (c == DELIM_START) {
                start = i;
            }
            if (c == DELIM_END) {
                end = i;
                break;
            }
            i++;
        }
        if (start == 0) {
            return url;
        }
        if (end < 1) {
            throw new IllegalArgumentException();
        }
        return url.substring(0, start) + "**" + url.substring(end + 1);
    }

    public String getUrl(Method method) {
        return methodUrlMap.get(method);
    }
}
