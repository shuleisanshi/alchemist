package com.yangbingdong.example.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yangbingdong.auth.config.AuthProperty;
import com.yangbingdong.auth.jwt.JwtOperator;
import com.yangbingdong.example.ExampleApplicationTests;
import com.yangbingdong.example.controller.AuthController;
import com.yangbingdong.example.controller.MyJwtPayload;
import com.yangbingdong.mvc.Response;
import com.youngbingdong.redisoper.extend.commom.CommonRedisoper;
import com.youngbingdong.util.jwt.AuthUtil;
import com.youngbingdong.util.jwt.Jwt;
import com.youngbingdong.util.jwt.JwtPayload;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.yangbingdong.auth.AuthorizeConstant.SESSION_EXPIRATION_SECOND;
import static com.youngbingdong.util.jwt.AuthUtil.AUTHORIZATION_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ybd
 * @date 19-5-24
 * @contact yangbingdong1994@gmail.com
 */
@AutoConfigureMockMvc
public class AuthControllerWithSessionTest extends ExampleApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtOperator jwtOperator;

	@Autowired
	private CommonRedisoper commonRedisoper;

    @Autowired
    private AuthProperty authProperty;

	@Test
	public void loginSuccessAndExpectHavingTokenInHeader() throws Exception {
		String name = "yangbingdong";
		mockMvc.perform(post("/auth/login/" + name).accept(APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andDo(print())
			   .andExpect(header().exists(AUTHORIZATION_HEADER))
			   .andExpect(result -> {
				   String authJwt = result.getResponse().getHeader(AUTHORIZATION_HEADER);
				   assertThat(authJwt).isNotNull();
                   Jwt<? extends JwtPayload> jwt = AuthUtil.parseAuthJwt(authJwt, authProperty.getSignKey(), MyJwtPayload.class);
                   assertThat(((MyJwtPayload)jwt.getPayload()).getName()).isNotNull()
                                                              .isEqualTo(name);
			   });
	}

	@Test
	public void loginSuccessAndExpectSessionKeyInRedisAndLocalCache() throws Exception {
		String header = loginAndGetAuthHeader();
		assertSessionInCache(header);
	}

	@Test
	public void accessAuthUrlWithAuthHeaderAndExpectRightBody() throws Exception {
		String header = loginAndGetAuthHeader();
		getAuthInfoAndExpectRight(header);

	}

	private void getAuthInfoAndExpectRight(String header) throws Exception {
		mockMvc.perform(get("/auth/info").header(AUTHORIZATION_HEADER, header))
			   .andExpect(status().is2xxSuccessful())
			   .andExpect(result -> {
				   String content = result.getResponse().getContentAsString();
				   assertThat(content).isNotNull();
				   Response<JSONObject> response = JSONObject.parseObject(content, new TypeReference<Response<JSONObject>>(){});
				   assertThat(response.getBody()).isEqualTo(AuthController.JSON_OBJECT);
			   });
	}

	@Test
	public void accessAuthUrlWithIllegalAuthTokenHeaderAndExpect401ResultCode() throws Exception {
		String header = loginAndGetAuthHeader();
		header = header.substring(0, header.length() - 2);
		MvcResult mvcResult = mockMvc.perform(get("/auth/info").header(AUTHORIZATION_HEADER, header))
									 .andExpect(status().is4xxClientError())
									 .andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		Response response = JSONObject.parseObject(content, Response.class);
		assertThat(response).isNotNull();
		assertThat(response.getBody()).isNull();
		assertThat(response.getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void accessAuthUtlWithoutAuthTokenAndExpect401UnAuthorized() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/auth/info"))
									 .andExpect(status().is4xxClientError())
									 .andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		Response response = JSONObject.parseObject(content, Response.class);
		assertThat(response).isNotNull();
		assertThat(response.getBody()).isNull();
		assertThat(response.getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void logoutAndExpectSessionRemoveFromRedis() throws Exception {
		String header = loginAndGetAuthHeader();
		String sessionExpKey = assertSessionInCache(header);

		mockMvc.perform(post("/auth/logout").header(AUTHORIZATION_HEADER, header))
			   .andExpect(status().isOk());
		Boolean existSessionInRedis = commonRedisoper.exists(sessionExpKey);
		assertThat(existSessionInRedis).isFalse();
		Long ttlInLocalCache = jwtOperator.getSessionTtlCache().getIfPresent(sessionExpKey);
		assertThat(ttlInLocalCache).isNull();
	}

	@Test
	public void logoutFullyAndExpectUnAuthorized() throws Exception {
		String header = loginAndGetAuthHeader();
		String sessionExpKey = assertSessionInCache(header);

		mockMvc.perform(post("/auth/logout-fully").header(AUTHORIZATION_HEADER, header))
			   .andExpect(status().isOk());
		Boolean existSessionInRedis = commonRedisoper.exists(sessionExpKey);
		assertThat(existSessionInRedis).isFalse();
		Long ttlInLocalCache = jwtOperator.getSessionTtlCache().getIfPresent(sessionExpKey);
		assertThat(ttlInLocalCache).isNull();

		MvcResult mvcResult = mockMvc.perform(get("/auth/info").header(AUTHORIZATION_HEADER, header))
									 .andExpect(status().is4xxClientError())
									 .andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		Response response = JSONObject.parseObject(content, Response.class);
		assertThat(response).isNotNull();
		assertThat(response.getBody()).isNull();
		assertThat(response.getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void loginAndCleanLocalSessionAndExpectAccessRight() throws Exception {
		String authJwt = loginAndGetAuthHeader();
		String signature = AuthUtil.getSignFromAuthJwt(authJwt);
		jwtOperator.cleanLocalSession(jwtOperator.getSessionExpKey(signature));
		getAuthInfoAndExpectRight(authJwt);
	}

	private String assertSessionInCache(String authJwt) {
		String signature = AuthUtil.getSignFromAuthJwt(authJwt);
		String sessionExpKey = jwtOperator.getSessionExpKey(signature);
		Boolean existSessionInRedis = commonRedisoper.exists(sessionExpKey);
		assertThat(existSessionInRedis).isTrue();
		Long ttlInLocalCache = jwtOperator.getSessionTtlCache().getIfPresent(sessionExpKey);
		assertThat(ttlInLocalCache).isEqualTo(SESSION_EXPIRATION_SECOND);
		return sessionExpKey;
	}

	private String loginAndGetAuthHeader() throws Exception {
		String header = mockMvc.perform(post("/auth/login/666").accept(APPLICATION_JSON))
							   .andReturn()
							   .getResponse()
							   .getHeader(AUTHORIZATION_HEADER);
		assertThat(header).isNotNull();
		return header;
	}

}
