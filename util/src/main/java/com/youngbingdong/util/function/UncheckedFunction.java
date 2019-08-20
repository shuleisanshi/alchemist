package com.youngbingdong.util.function;

/**
 * @author ybd
 * @date 17-11-28.
 */
@FunctionalInterface
public interface UncheckedFunction<T, R> {
	R apply(T t) throws Exception;
}
