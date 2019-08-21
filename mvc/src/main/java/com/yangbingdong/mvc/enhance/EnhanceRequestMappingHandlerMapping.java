package com.yangbingdong.mvc.enhance;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * @author ybd
 * @date 2019/8/21
 * @contact yangbingdong1994@gmail.com
 */
public class EnhanceRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    public static final String X_INNER_ACTION = "X-Inner-Action";

    private Map<String, RequestMappingInfoHandlerMethodPair> urlPairLookup;

    @Override
    protected void handlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
        super.handlerMethodsInitialized(handlerMethods);
        urlPairLookup = new HashMap<>(handlerMethods.size());
        handlerMethods.forEach((k, v) -> {
            Set<String> pathPatterns = getMappingPathPatterns(k);
            if (pathPatterns.size() > 1) {
                throw new IllegalArgumentException("Not allow multi paths");
            }
            String path = new ArrayList<>(pathPatterns).get(0);
            if (getPathMatcher().isPattern(path)) {
                if (k.getName() == null) {
                    throw new IllegalArgumentException("Pattern path must have a name");
                }
                path = k.getName();
            }
            RequestMappingInfoHandlerMethodPair pair = buildPair(k, v);
            urlPairLookup.put(path, pair);
        });
    }

    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) {
        String lookupPathKey = defaultIfNull(request.getHeader(X_INNER_ACTION), lookupPath);
        RequestMappingInfoHandlerMethodPair pair = urlPairLookup.get(lookupPathKey);
        if (pair == null) {
            return null;
        }
        request.setAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE, pair.handlerMethod);
        handleMatch(pair.requestMappingInfo, lookupPath, request);
        return pair.handlerMethod;
    }

    private RequestMappingInfoHandlerMethodPair buildPair(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) {
        return new RequestMappingInfoHandlerMethodPair(requestMappingInfo, handlerMethod);
    }

    @Data
    @AllArgsConstructor
    private static class RequestMappingInfoHandlerMethodPair {
        private RequestMappingInfo requestMappingInfo;
        private HandlerMethod handlerMethod;
    }
}
