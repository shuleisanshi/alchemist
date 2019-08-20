package com.youngbingdong.redisoper.core.metadata.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ybd
 * @date 19-3-28
 * @contact yangbingdong1994@gmail.com
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface RedisPrimaryKey {
	int order() default 0;

}
