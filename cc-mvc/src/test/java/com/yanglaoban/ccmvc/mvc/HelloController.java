package com.yanglaoban.ccmvc.mvc;

import com.yanglaoban.ccmvc.annotated.Rest;
import com.yanglaoban.ccmvc.log.core.OpLog;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ybd
 * @date 19-5-21
 * @contact yangbingdong1994@gmail.com
 */
@Rest
public class HelloController {

	@OpLog("SayHello")
	@GetMapping("/hello")
	public void sayHello() {
		System.out.println("####### Hello World!");
	}

	@GetMapping("/error")
	public void mockError() {
		throw new RuntimeException("Mock Error!");
	}
}
