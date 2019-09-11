package com.yangbingdong.mvc.log.core;

import com.yangbingdong.mvc.log.OpLogContext;
import org.aspectj.lang.JoinPoint;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
public interface OpLogContextBuilder<T extends OpLogContext> {

	T buildContext(JoinPoint joinPoint, OpLog opLog, Object returnValue, Throwable exception);

}
