package com.youngboss.ccredisoper.extend.dlock.annotated;

import com.youngboss.ccredisoper.extend.dlock.DLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.lang.reflect.Method;

import static com.youngboss.ccutil.spring.SpELParser.parseSpEL;


/**
 * @author ybd
 * @date 18-8-2
 * @contact yangbingdong1994@gmail.com
 */
@Aspect
public class DLockAspect {
	@Resource
	private DLock dLock;

	@Value("${spring.application.name:default}")
	private String namespace;

	@Around(value = "@annotation(lock)")
	public Object doAround(ProceedingJoinPoint pjp, Lock lock) throws Throwable {
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		Object[] args = pjp.getArgs();
		String keySpEL = lock.key();
		String resourceKey = parseSpEL(method, args, keySpEL, String.class);
		String finalKey = buildFinalKey(resourceKey);
		return dLock.tryLock(finalKey, lock.waitSecond(), lock.leaseSecond(), () -> pjp.proceed(pjp.getArgs()));
	}

	private String buildFinalKey(String key) {
		return "DLock:" + namespace + ":" + key;
	}


}
