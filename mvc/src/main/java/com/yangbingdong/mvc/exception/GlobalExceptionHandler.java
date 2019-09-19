package com.yangbingdong.mvc.exception;

import cn.hutool.core.text.StrFormatter;
import com.yangbingdong.mvc.Response;
import com.yangbingdong.mvc.config.MvcProperty;
import com.youngbingdong.util.exception.BusinessException;
import com.youngbingdong.util.jwt.TokenException;
import com.youngbingdong.util.jwt.TokenExpireException;
import com.youngbingdong.util.spring.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static cn.hutool.core.util.CharsetUtil.UTF_8;
import static cn.hutool.core.util.ObjectUtil.defaultIfNull;
import static cn.hutool.core.util.StrUtil.EMPTY_JSON;
import static cn.hutool.http.HttpUtil.decodeParamMap;
import static com.alibaba.fastjson.JSON.parseObject;
import static com.yangbingdong.mvc.Response.error;
import static com.youngbingdong.util.constant.CommonRespCode.TOKEN_EXPIRE;
import static com.youngbingdong.util.constant.CommonRespCode.UN_AUTH;
import static com.youngbingdong.util.spring.RequestHolder.currentRequestHeader;
import static com.youngbingdong.util.spring.RequestHolder.getRequestBody;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author ybd
 * @date 17-12-12
 * <p>
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
@EnableConfigurationProperties(MvcProperty.class)
public class GlobalExceptionHandler {

    @Autowired
    private MvcProperty mvcProperty;

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
		return error(validateFailReason);
	}

	@ExceptionHandler(value = BusinessException.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public Response<Void> businessExceptionHandler(BusinessException ex) {
        if (mvcProperty.isPrintRequestInfoIfError()) {
            String info = getRequestInfoFormatString("业务异常捕获");
            log.error(info, ex);
            return error(ex);
        }
		log.error("业务异常捕获: " + ex.getMessage());
		return error(ex);
	}

	@ExceptionHandler(value = NoHandlerFoundException.class)
	@ResponseStatus(NOT_FOUND)
	public Response<Void> notFoundExceptionHandler(NoHandlerFoundException ex) {
		return error(ex, NOT_FOUND.value());
	}

	@ExceptionHandler(value = TokenException.class)
	@ResponseStatus(UNAUTHORIZED)
	public Response<Void> tokenExceptionHandler(TokenException ex) {
		log.error("Token校验异常捕获: " + ex.getMessage());
		return error(ex.getMessage(), UN_AUTH);
	}

    @ExceptionHandler(value = TokenExpireException.class)
    @ResponseStatus(UNAUTHORIZED)
    public Response<Void> tokenExpireExceptionHandler(TokenExpireException ex) {
        log.error("Token过期异常捕获: " + ex.getMessage());
        return error(ex.getMessage(), TOKEN_EXPIRE);
    }

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public Response<Void> defaultErrorHandler(Exception ex) {
        if (mvcProperty.isPrintRequestInfoIfError()) {
            String info = getRequestInfoFormatString("全局异常捕获");
            log.error(info, ex);
            return error(ex);
        }
        log.error("全局异常捕获, ", ex);
        return error(ex);
	}

    private String getRequestInfoFormatString(final String name) {
        HttpServletRequest httpServletRequest = RequestHolder.currentRequest();
        return StrFormatter.format(
                name + ", Request Info:\nmethod: {}\nuri   : {}\nparam : {}\nbody  : {}\nheader: {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI(),
                decodeParamMap(httpServletRequest.getQueryString(), UTF_8),
                defaultIfNull(parseObject(getRequestBody()), EMPTY_JSON),
                currentRequestHeader());
    }
}
