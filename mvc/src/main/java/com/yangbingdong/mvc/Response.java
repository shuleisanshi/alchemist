package com.yangbingdong.mvc;

import com.youngbingdong.util.time.SystemTimer;
import lombok.Data;
import lombok.experimental.Accessors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author ybd
 * @date 18-5-15
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class Response<T> {
	private boolean success = true;
	private T body;
	private String errorMsg;
	private int code = OK.value();
	private long currentDate = SystemTimer.now();

	@SuppressWarnings("unchecked")
	public static <T> Response<T> ok(T body) {
		return body instanceof Response ? (Response<T>) body : new Response<T>().setBody(body);
	}

	public static Response<Void> ok() {
		return new Response<>();
	}

	public static Response<Void> error(Exception e, int code) {
		return new Response<Void>().setSuccess(false)
								   .setErrorMsg(e.getMessage())
								   .setCode(code);
	}

	public static Response<Void> error(Exception e) {
		return error(e, INTERNAL_SERVER_ERROR.value());
	}

	public static Response<Void> error(String errorMsg) {
		return error(errorMsg, INTERNAL_SERVER_ERROR.value());
	}

	public static Response<Void> error(String errorMsg, int code) {
		return new Response<Void>().setSuccess(false)
								   .setErrorMsg(errorMsg)
								   .setCode(code);
	}
}
