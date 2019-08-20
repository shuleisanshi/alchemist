package com.youngbingdong.util.function;

/**
 * @author ybd
 * @date 17-11-28.
 */
public interface UncheckedPredicate<T> {
	boolean test(T t) throws Exception;
}
