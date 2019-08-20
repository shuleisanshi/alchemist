package com.youngbingdong.ccutil;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

/**
 * @author ybd
 * @date 19-4-15
 * @contact yangbingdong1994@gmail.com
 */
public final class BigDecimalUtil {

	public static boolean eqZero(BigDecimal target) {
		return ZERO.compareTo(target) == 0;
	}

	public static boolean gtZero(BigDecimal target) {
		return ZERO.compareTo(target) < 0;
	}

	public static boolean ltZero(BigDecimal target) {
		return ZERO.compareTo(target) > 0;
	}
}
