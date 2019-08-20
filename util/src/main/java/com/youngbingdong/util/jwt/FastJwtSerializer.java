package com.youngbingdong.util.jwt;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;

import java.util.Map;

/**
 * @author ybd
 * @date 19-4-9
 * @contact yangbingdong1994@gmail.com
 */
public class FastJwtSerializer implements Serializer<Map<String,?>> {
	@Override
	public byte[] serialize(Map<String, ?> stringMap) throws SerializationException {
		return JSONObject.toJSONBytes(stringMap);
	}
}
