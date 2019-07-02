package com.youngboss.ccutil;


/**
 * @author ybd
 * @date 18-8-24
 * @contact yangbingdong1994@gmail.com
 */
public class NumericConvertUtil {
	/**
	 * 在进制表示中的字符集合，0-Z分别用于表示最大为62进制的符号表示
	 */
	private static final char[] DIGITS62 = {
			'U', 'K', 'F', 'W', 'T', 'O', 'B', 'i', '7', 'P',
			'V', '6', 'e', 's', 'S', 'b', '8', 'o', 'v', 'H',
			'Y', 'p', 'I', 'c', 'x', 'L', 'y', '0', 'D', 'M',
			'q', '3', 'J', 'h', 'k', 'u', 'N', '5', 'j', 'A',
			'r', 'n', 'g', 'd', '1', 'R', '9', 'w', 'l', '4',
			'X', 'C', 'm', 'E', 'a', 'z', '2', 'Z', 't', 'Q',
			'f', 'G'
	};


	private static final int MAX_SEED_62 = DIGITS62.length;

	public static String convert10To62System(long number) {
		return toOtherNumberSystem(number, MAX_SEED_62);
	}


	public static long convert62To10System(String number) {
		return toDecimalNumber(number, MAX_SEED_62);
	}

	/**
	 * 将十进制的数字转换为指定进制的字符串
	 *
	 * @param number 十进制的数字
	 * @param seed   指定的进制
	 * @return 指定进制的字符串
	 */
	public static String toOtherNumberSystem(long number, int seed) {
		if (number < 0) {
			number = ((long) 2 * 0x7fffffff) + number + 2;
		}
		char[] buf = new char[32];
		int charPos = 32;
		while ((number / seed) > 0) {
			buf[--charPos] = DIGITS62[(int) (number % seed)];
			number /= seed;
		}
		buf[--charPos] = DIGITS62[(int) (number % seed)];
		return new String(buf, charPos, (32 - charPos));
	}

	/**
	 * 将其它进制的数字（字符串形式）转换为十进制的数字
	 *
	 * @param number 其它进制的数字（字符串形式）
	 * @param seed   指定的进制，也就是参数str的原始进制
	 * @return 十进制的数字
	 */
	public static long toDecimalNumber(String number, int seed) {
		char[] charBuf = number.toCharArray();
		if (seed == 10) {
			return Long.parseLong(number);
		}

		long result = 0, base = 1;

		for (int i = charBuf.length - 1; i >= 0; i--) {
			int index = 0;
			for (int j = 0, length = DIGITS62.length; j < length; j++) {
				//找到对应字符的下标，对应的下标才是具体的数值
				if (DIGITS62[j] == charBuf[i]) {
					index = j;
				}
			}
			result += index * base;
			base *= seed;
		}
		return result;
	}
}
