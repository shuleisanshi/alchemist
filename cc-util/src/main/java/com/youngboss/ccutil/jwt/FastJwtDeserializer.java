package com.youngboss.ccutil.jwt;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.io.Deserializer;

import java.util.Map;

/**
 * @author ybd
 * @date 19-4-9
 * @contact yangbingdong1994@gmail.com
 */
public class FastJwtDeserializer implements Deserializer<Map<String,?>> {
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> deserialize(byte[] bytes) throws DeserializationException {
		return (Map<String, ?>) JSONObject.parse(bytes);
	}
}
