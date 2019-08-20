package com.youngbingdong.util;

/**
 * @author ybd
 * @date 19-4-23
 * @contact yangbingdong1994@gmail.com
 */
public final class PlaceHolderParser {

	private static final String DEFAULT_OPEN_TOKEN = "{";
	private static final String DEFAULT_CLOSE_TOKEN = "}";

	public static String parse(String openToken, String closeToken, String text, Object... args) {
		if (args == null || args.length <= 0) {
			return text;
		}
		int argsIndex = 0;

		if (text == null || text.isEmpty()) {
			return "";
		}
		char[] src = text.toCharArray();
		int offset = 0;
		int start = text.indexOf(openToken, offset);
		if (start == -1) {
			return text;
		}
		final StringBuilder builder = new StringBuilder();
		StringBuilder expression = null;
		while (start > -1) {
			if (start > 0 && src[start - 1] == '\\') {
				builder.append(src, offset, start - offset - 1).append(openToken);
				offset = start + openToken.length();
			} else {
				if (expression == null) {
					expression = new StringBuilder();
				} else {
					expression.setLength(0);
				}
				builder.append(src, offset, start - offset);
				offset = start + openToken.length();
				int end = text.indexOf(closeToken, offset);
				while (end > -1) {
					if (end > offset && src[end - 1] == '\\') {
						expression.append(src, offset, end - offset - 1).append(closeToken);
						offset = end + closeToken.length();
						end = text.indexOf(closeToken, offset);
					} else {
						expression.append(src, offset, end - offset);
						break;
					}
				}
				if (end == -1) {
					builder.append(src, start, src.length - start);
					offset = src.length;
				} else {
					String value = (argsIndex <= args.length - 1) ?
							(args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
					builder.append(value);
					offset = end + closeToken.length();
					argsIndex++;
				}
			}
			start = text.indexOf(openToken, offset);
		}
		if (offset < src.length) {
			builder.append(src, offset, src.length - offset);
		}
		return builder.toString();
	}

	public static String parse(String text, Object... args) {
		return parse(DEFAULT_OPEN_TOKEN, DEFAULT_CLOSE_TOKEN, text, args);
	}

}
