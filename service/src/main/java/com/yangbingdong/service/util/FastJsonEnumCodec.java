package com.yangbingdong.service.util;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.toolkit.EnumUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ybd
 * @date 2019/9/2
 * @contact yangbingdong1994@gmail.com
 */
public class FastJsonEnumCodec implements ObjectSerializer, ObjectDeserializer {

    private static ConcurrentHashMap<Class<?>, Method> methodCache = new ConcurrentHashMap<>(16);

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object value = parser.parse();
        Class enumClass = (Class) type;
        Method getValueMethod = getMethod(enumClass);
        Enum enumeration = EnumUtils.valueOf(enumClass, value, getValueMethod);
        return (T) enumeration;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            serializer.getWriter().writeNull();
            return;
        }
        IEnum enumeration = (IEnum) object;
        out.write(enumeration.getValue().toString());
    }

    private static Method getMethod(Class<?> clazz) {
        Method method = methodCache.get(clazz);
        if (method != null) {
            return method;
        }
        try {
            method = clazz.getDeclaredMethod("getValue");
            methodCache.put(clazz, method);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
