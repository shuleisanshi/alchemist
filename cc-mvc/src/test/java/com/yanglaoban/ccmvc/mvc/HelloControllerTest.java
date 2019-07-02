package com.yanglaoban.ccmvc.mvc;

import com.alibaba.fastjson.JSONObject;
import com.yanglaoban.ccmvc.CcMvcApplicationTests;
import com.yanglaoban.ccmvc.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ybd
 * @date 19-5-21
 * @contact yangbingdong1994@gmail.com
 */
@AutoConfigureMockMvc
public class HelloControllerTest extends CcMvcApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void helloTestUsingMockMvc() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/hello").accept(APPLICATION_JSON))
									 .andExpect(status().isOk())
									 .andReturn();
		Response response = extractResponse(mvcResult);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.getCode()).isEqualTo(200);
	}

	@Test
	public void code404Test() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/not-exist-url").accept(APPLICATION_JSON))
									 .andExpect(status().isNotFound())
									 .andReturn();
		Response response = extractResponse(mvcResult);
		assertThat(response.isSuccess()).isFalse();
		assertThat(response.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void errorResponseTest() throws Exception {
		mockMvc.perform(get("/error").accept(APPLICATION_JSON))
			   .andExpect(status().is5xxServerError());
	}

	private Response extractResponse(MvcResult mvcResult) throws UnsupportedEncodingException {
		return JSONObject.parseObject(mvcResult.getResponse().getContentAsString(), Response.class);
	}
}
