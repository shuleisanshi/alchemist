package com.yangbingdong.mvc.log;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
@Slf4j
public class OpLogContext {

	private static final int MAX_LENGTH_2048 = 2048;
	private static final int MAX_LENGTH_1024 = 1024;

	private LocalDateTime opTime;
	private JoinPoint joinPoint;
	private String desc;
	private String ip;
	private String url;
	private String userAgent;
    private String returnValue;
	private Throwable exception;

	private String operatingSystem;
	private String browserVersion;
	private String receiveData;

	public OpLogContext parseOperatingSystemAndBrowser() {
        UserAgent userAgent = UserAgentUtil.parse(this.userAgent);
		if ("Unknown".equalsIgnoreCase(userAgent.getOs().getName())
				&& "Unknown".equalsIgnoreCase(userAgent.getBrowser().getName())) {
			this.operatingSystem = this.userAgent;
			return this;
		}
		this.operatingSystem = userAgent.getOs().getName();
		this.browserVersion = userAgent.getBrowser().getName() + userAgent.getVersion();
		return this;
	}

	public OpLogContext parseReceiveData() {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String[] paramNames = methodSignature.getParameterNames();
		int length = paramNames.length;
		if (length == 0) {
			receiveData = "";
			return this;
		}
		Object[] paramValues = joinPoint.getArgs();
		Assert.isTrue(paramValues.length == length, "parse error");
		JSONObject receiveData = new JSONObject();
		for (int i = 0; i < length; i++) {
			if (skipAppendParam(paramValues[i])) {
				continue;
			}
			receiveData.put(paramNames[i], paramValues[i]);
		}
		String data = toJSONString(receiveData);
		this.receiveData = cropString(data, MAX_LENGTH_2048);
		return this;
	}

	private String cropString(String s, int bound) {
		if (s == null) {
			s = "";
		} else {
			if (s.length() > bound) {
				s = s.substring(0, bound - 5) + "...";
			}
		}
		return s;
	}

	private boolean skipAppendParam(Object paramValue) {
		return paramValue instanceof HttpServletRequest
				|| paramValue instanceof HttpServletResponse
				|| paramValue instanceof HttpSession
				|| paramValue instanceof BindingResult;
	}
}
