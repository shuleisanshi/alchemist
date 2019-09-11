package com.yangbingdong.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.yangbingdong.auth.annotated.IgnoreAuth;
import com.yangbingdong.example.user.domain.entity.TestUser;
import com.yangbingdong.example.user.domain.service.TestUserService;
import com.yangbingdong.mvc.annotated.Rest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ybd
 * @date 2019/9/9
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/user")
@Slf4j
public class UserController {

    @Autowired
    private TestUserService testUserService;

    @IgnoreAuth
    @GetMapping("/get")
    public TestUser get(String id) {
        return testUserService.getById(id);
    }

    @IgnoreAuth
    @PostMapping("/post")
    public void multiBody(String name, @RequestBody JSONObject json) {
        throw new IllegalArgumentException("Mock");
    }
}
