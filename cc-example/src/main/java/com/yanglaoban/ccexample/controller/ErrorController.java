package com.yanglaoban.ccexample.controller;

import com.yanglaoban.ccauth.annotated.IgnoreAuth;
import com.yanglaoban.ccmvc.annotated.Rest;
import com.youngboss.ccutil.exception.AssertUtils;
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
