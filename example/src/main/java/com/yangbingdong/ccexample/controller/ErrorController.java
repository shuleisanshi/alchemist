package com.yangbingdong.ccexample.controller;

import com.yangbingdong.ccauth.annotated.IgnoreAuth;
import com.yangbingdong.ccmvc.annotated.Rest;
import com.youngbingdong.ccutil.exception.AssertUtils;
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
		AssertUtils.throwBusiException("This is mock exception");
	}
}
