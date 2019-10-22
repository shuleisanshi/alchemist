package com.yangbingdong.example.controller;

import com.yangbingdong.auth.annotated.IgnoreAuth;
import com.yangbingdong.mvc.annotated.Rest;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ybd
 * @date 2019/10/21
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/hello")
public class HelloController {

    @IgnoreAuth
    @GetMapping
    public String helloWorld(String name) {
        return "Hello " + name + "!";
    }
}
