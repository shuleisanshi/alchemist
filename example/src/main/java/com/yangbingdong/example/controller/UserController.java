package com.yangbingdong.example.controller;

import com.yangbingdong.auth.annotated.IgnoreAuth;
import com.yangbingdong.example.user.domain.entity.TestUser;
import com.yangbingdong.example.user.domain.service.TestUserService;
import com.yangbingdong.mvc.annotated.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ybd
 * @date 2019/9/9
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/user")
public class UserController {

    @Autowired
    private TestUserService testUserService;

    @IgnoreAuth
    @GetMapping("/get")
    public TestUser get(String id) {
        return testUserService.getById(id);
    }
}
