package com.yangbingdong.ccmvc.log.core;

import com.yangbingdong.ccmvc.log.OpLogContext;
import org.aspectj.lang.JoinPoint;

/**
 * @author ybd
 * @date 19-5-7
 * @contact yangbingdong1994@gmail.com
 */
public interface OpLogContextBuilder<T extends OpLogContext> {

	T buildContext(JoinPoint joinPoint, OpLog opLog, Throwable exception);

}