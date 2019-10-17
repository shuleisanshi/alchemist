package com.youngbingdong.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ybd
 * @date 2019/10/16
 * @contact yangbingdong1994@gmail.com
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MergeColumn {

    String[] sameAs() default {};

}
