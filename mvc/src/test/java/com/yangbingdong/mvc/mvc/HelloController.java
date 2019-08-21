package com.yangbingdong.mvc.mvc;

import com.yangbingdong.mvc.annotated.Rest;
import com.yangbingdong.mvc.log.core.OpLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ybd
 * @date 19-5-21
 * @contact yangbingdong1994@gmail.com
 */
@Rest
public class HelloController {

	@OpLog("SayHello")
	@GetMapping(value = "/hello/{name}", name = "helloWorld")
	public void sayHello(@PathVariable String name) {
		System.out.println("####### Hello World! Your name is " + name);
	}

	@GetMapping("/error")
	public void mockError() {
		throw new RuntimeException("Mock Error!");
	}
}
