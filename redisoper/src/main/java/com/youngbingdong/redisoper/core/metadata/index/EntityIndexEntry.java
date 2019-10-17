package com.youngbingdong.redisoper.core.metadata.index;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ybd
 * @date 19-3-23
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class EntityIndexEntry {

	private String indexField;

	private String indexFieldCap;

	private int order;

	private boolean unique = false;

    private Method readMethod;

    private Method writeMethod;

    public Object read(Object entity) {
        try {
            return readMethod.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Object entity, Object... values) {
        try {
            writeMethod.invoke(entity, values);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
