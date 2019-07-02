package com.youngboss.ccutil.reflect;

import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * @author ybd
 * @date 19-3-23
 * @contact yangbingdong1994@gmail.com
 */
public class BeanUtil {

	private BeanUtil() {
	}

	public static void copyPropertiesIgnoreNull(Object source, Object target) {
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
}
