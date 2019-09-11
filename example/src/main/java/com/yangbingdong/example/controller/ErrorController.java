package com.yangbingdong.example.controller;

import com.yangbingdong.auth.annotated.IgnoreAuth;
import com.yangbingdong.mvc.annotated.Rest;
import com.youngbingdong.util.exception.AssertUtils;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ybd
 * @date 19-5-24
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/error")
public class ErrorController {

	@IgnoreAuth
	@GetMapping("/mock")
	public void mockException() {
		AssertUtils.throwBusinessException("This is mock exception");
	}
}
