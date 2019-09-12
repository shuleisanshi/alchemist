package com.yangbingdong.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.yangbingdong.auth.annotated.IgnoreAuth;
import com.yangbingdong.auth.jwt.JwtOperator;
import com.yangbingdong.mvc.annotated.Rest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author ybd
 * @date 19-5-23
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/auth")
@Slf4j
public class AuthController {

	public static final JSONObject JSON_OBJECT = new JSONObject().fluentPut("key", "yes");

	@Autowired
	private JwtOperator jwtOperator;

	@IgnoreAuth
	@PostMapping("/login/{name}")
	public void login(@PathVariable String name) {
        MyJwtPayload myJwtPayload = new MyJwtPayload();
		jwtOperator.grantAuthJwt(myJwtPayload.setName(name));
	}

	@GetMapping("/info")
	public JSONObject authReq() {
		return JSON_OBJECT;
	}

	@PostMapping("/logout")
	public void logout() {
		jwtOperator.eraseSession();
	}

	@PostMapping("/logout-fully")
	public void fullyLogout() {
		jwtOperator.eraseSession();
	}
}
