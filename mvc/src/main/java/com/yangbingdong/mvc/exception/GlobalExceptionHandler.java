package com.yangbingdong.mvc.exception;

import com.yangbingdong.mvc.Response;
import com.youngbingdong.util.exception.BusiException;
import com.youngbingdong.util.jwt.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author ybd
 * @date 17-12-12
 * <p>
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@SuppressWarnings("ConstantConditions")
	@ExceptionHandler(value = {
			MethodArgumentNotValidException.class,
			BindException.class,
			ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response<Void> validExceptionHandler(Exception ex) {
		String validateFailReason;
		if (ex instanceof MethodArgumentNotValidException) {
			validateFailReason = ((MethodArgumentNotValidException) ex).getBindingResult()
																	   .getFieldError()
																	   .getDefaultMessage();
		} else if (ex instanceof BindException) {
			validateFailReason = ((BindException) ex).getFieldError().getDefaultMessage();
		} else if (ex instanceof ConstraintViolationException) {
			validateFailReason = ((ConstraintViolationException) ex).getConstraintViolations().stream()
																	.findAny()
																	.map(ConstraintViolation::getMessage)
																	.orElse("Unknown error message");
		} else {
			validateFailReason = "Unknown error message";
		}
		return Response.error(validateFailReason);
	}

	@ExceptionHandler(value = BusiException.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public Response<Void> busiExceptionHandler(BusiException ex) {
		log.error("业务异常捕获: " + ex.getMessage());
		return Response.error(ex);
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	@ResponseStatus(NOT_FOUND)
	public Response<Void> notFoundExceptionHandler(NoHandlerFoundException ex) {
		return Response.error(ex, NOT_FOUND.value());
	}

	@ExceptionHandler(value = TokenException.class)
	@ResponseStatus(FORBIDDEN)
	public Response<Void> tokenExceptionHandler(TokenException ex) {
		log.error("Token校验异常捕获: " + ex.getMessage());
		return Response.error(ex.getMessage(), FORBIDDEN.value());
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public Response<Void> defaultErrorHandler(Exception ex) {
		log.error("全局异常捕获: ", ex);
		return Response.error(ex);
	}
}
