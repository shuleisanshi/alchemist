package com.youngbingdong.ccredisoper.serilize;

/**
 * @author ybd
 * @date 19-3-21
 * @contact yangbingdong1994@gmail.com
 */
public class SerializeException extends RuntimeException {
	private static final long serialVersionUID = -1340513446022242738L;

	public SerializeException(String message, Throwable cause) {
		super(message, cause);
	}
}
