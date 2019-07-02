package com.yanglaoban.ccmvc.log;

import com.yanglaoban.ccmvc.log.core.OpLog;
import com.yanglaoban.ccmvc.log.core.OpLogContextBuilder;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

import static com.yanglaoban.ccmvc.interceptor.IpInterceptor.IP;
import static com.youngboss.ccutil.spring.RequestHolder.currentRequest;
import static com.youngboss.ccutil.time.SystemTimer.nowDateTime;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
public class DefaultOpLogContextBuilder implements OpLogContextBuilder<OpLogContext> {

	@Override
	public OpLogContext buildContext(JoinPoint joinPoint, OpLog opLog, Throwable exception) {
		HttpServletRequest request = currentRequest();
		OpLogContext opLogContext = new OpLogContext();
		opLogContext.setJoinPoint(joinPoint)
					.setOpTime(nowDateTime())
					.setDesc(opLog.value())
					.setHttpServletRequest(request)
					.setIp((String) request.getAttribute(IP))
					.setUrl(request.getRequestURI())
					.setUserAgent(request.getHeader("User-Agent"))
					.setException(exception);
		return opLogContext;
	}
}
