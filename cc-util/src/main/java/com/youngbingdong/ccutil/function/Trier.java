package com.youngbingdong.ccutil.function;


import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * @author ybd
 * @date 17-11-28.
 * Lambda包装检测异常为非检测异常
 */
public final class Trier {
	public static <T, R> Function<T, R> tryFunction(UncheckedFunction<T, R> function) {
		requireNonNull(function);
		return t -> {
			try {
				return function.apply(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	public static <T, R> Function<T, R> tryFunction(UncheckedFunction<T, R> function, R defaultValue) {
		requireNonNull(function);
		return t -> {
			try {
				return function.apply(t);
			} catch (Exception e) {
				return defaultValue;
			}
		};
	}

	public static <T> Supplier<T> trySupplier(UncheckedSupplier<T> supplier) {
		requireNonNull(supplier);
		return () -> {
			try {
				return supplier.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	public static <T> Supplier<T> trySupplier(UncheckedSupplier<T> supplier, T defaultValue) {
		requireNonNull(supplier);
		return () -> {
			try {
				return supplier.get();
			} catch (Exception e) {
				return defaultValue;
			}
		};
	}

	public static <T> Consumer<T> tryConsumer(UncheckedConsumer<T> consumer) {
		requireNonNull(consumer);
		return t -> {
			try {
				consumer.accept(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	public static IntConsumer tryIntConsumer(UncheckedIntConsumer consumer) {
		requireNonNull(consumer);
		return i -> {
			try {
				consumer.accept(i);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	public static LongConsumer tryLongConsumer(UncheckedLongConsumer consumer) {
		requireNonNull(consumer);
		return l -> {
			try {
				consumer.accept(l);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	public static <T> Predicate<T> tryPredicate(UncheckedPredicate<T> predicate) {
		requireNonNull(predicate);
		return t -> {
			try {
				return predicate.test(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	public static <T> Predicate<T> tryPredicate(UncheckedPredicate<T> predicate, boolean defaultValue) {
		requireNonNull(predicate);
		return t -> {
			try {
				return predicate.test(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}
