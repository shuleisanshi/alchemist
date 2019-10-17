package com.youngbingdong.util.reflect;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import static cn.hutool.core.bean.BeanUtil.beanToMap;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * @author ybd
 * @date 19-3-23
 * @contact yangbingdong1994@gmail.com
 */
public class BeanUtil {

	private BeanUtil() {
	}

    /**
     * 复制属性, 忽略空值, 只支持相同 Class
     */
	public static void copyPropertiesWithSameClass(Object source, Object target) {
		Assert.isTrue(source.getClass() == target.getClass(), "Not support different class");
		PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());
		for (PropertyDescriptor pd : targetPds) {
			Method readMethod = pd.getReadMethod();
			if (readMethod != null) {
				try {
					Object value = readMethod.invoke(source);
					if (value == null) {
						continue;
					}
					Method writeMethod = pd.getWriteMethod();
					if (writeMethod != null) {
						writeMethod.invoke(target, value);
					}
				} catch (Throwable ex) {
					throw new FatalBeanException(
							"Could not copy property '" + pd.getName() + "' from source to target", ex);
				}
			}
		}
	}

    /**
     * 复制属性, 忽略空值, 只支持不同 Class
     */
    public static void copyProperties(Object source, Object target) {
        PropertyDescriptor[] sourcePds = getPropertyDescriptors(source.getClass());
        for (PropertyDescriptor pd : sourcePds) {
            Method readMethod = pd.getReadMethod();
            if (readMethod != null) {
                try {
                    Object value = readMethod.invoke(source);
                    if (value == null) {
                        continue;
                    }
                    PropertyDescriptor targetPd = BeanUtils.getPropertyDescriptor(target.getClass(), pd.getName());
                    if (targetPd != null) {
                        Method writeMethod = targetPd.getWriteMethod();
                        if (writeMethod != null) {
                            writeMethod.invoke(target, value);
                        }
                    }
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + pd.getName() + "' from source to target", ex);
                }
            }
        }
    }

    public static Map<String, Object> toMap(Object bean) {
        return beanToMap(bean, false, true);
    }
}
