package com.yangbingdong.mvc.log;

import com.yangbingdong.mvc.interceptor.IpInterceptor;
import com.yangbingdong.mvc.log.core.OpLog;
import com.yangbingdong.mvc.log.core.OpLogContextBuilder;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.youngbingdong.util.spring.RequestHolder.currentRequest;
import static com.youngbingdong.util.time.SystemTimer.nowDateTime;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
public class DefaultOpLogContextBuilder implements OpLogContextBuilder<OpLogContext> {

	@Override
	public OpLogContext buildContext(JoinPoint joinPoint, OpLog opLog, Object returnValue, Throwable exception) {
		HttpServletRequest request = currentRequest();
		OpLogContext opLogContext = new OpLogContext();
        opLogContext.setJoinPoint(joinPoint)
                    .setOpTime(nowDateTime())
                    .setDesc(opLog.value())
                    .setIp((String) request.getAttribute(IpInterceptor.IP))
                    .setUrl(request.getRequestURI())
                    .setUserAgent(request.getHeader("User-Agent"))
                    .setReturnValue(toJSONString(returnValue))
                    .setException(exception);
		return opLogContext;
	}
}
