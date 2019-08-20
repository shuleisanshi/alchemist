package com.yangbingdong.example.test;

import com.yangbingdong.example.ExampleApplicationTests;
import com.yangbingdong.mvc.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.alibaba.fastjson.JSON.parseObject;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ybd
 * @date 19-5-24
 * @contact yangbingdong1994@gmail.com
 */
@AutoConfigureMockMvc
public class ErrorExceptionTest extends ExampleApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void expectError() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/error/mock"))
			   .andExpect(status().is5xxServerError())
			   .andDo(MockMvcResultHandlers.print())
			   .andExpect(result -> {
				   String content = result.getResponse().getContentAsString();
				   Response response = parseObject(content, Response.class);
				   assertFalse(response.isSuccess());
				   Assertions.assertEquals(response.getCode(), INTERNAL_SERVER_ERROR.value());
			   });
	}
}
