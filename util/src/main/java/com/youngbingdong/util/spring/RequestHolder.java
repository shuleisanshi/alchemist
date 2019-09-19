package com.youngbingdong.util.spring;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

/**
 * @author ybd
 * @date 19-4-9
 * @contact yangbingdong1994@gmail.com
 */
public class RequestHolder {
	@SuppressWarnings("ConstantConditions")
	public static HttpServletResponse currentResponse() {
		return ((ServletRequestAttributes) getRequestAttributes()).getResponse();
	}

	@SuppressWarnings("ConstantConditions")
	public static HttpServletRequest currentRequest() {
		return ((ServletRequestAttributes) getRequestAttributes()).getRequest();
	}

    public static String getRequestBody() {
        RequestBodyCachingWrapper wrapper = WebUtils.getNativeRequest(currentRequest(), RequestBodyCachingWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getBody();
            if (buf.length > 0) {
                return new String(buf);
            }
        }
        return StrUtil.EMPTY;
    }

    public static Map<String, String> currentRequestHeader() {
        HttpServletRequest request = currentRequest();
        Map<String, String> map = new HashMap<>(16);
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
