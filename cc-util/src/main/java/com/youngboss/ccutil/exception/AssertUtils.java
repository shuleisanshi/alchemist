package com.youngboss.ccutil.exception;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author qww
 * @date 18-12-27
 */
public class AssertUtils {
    public static void throwBusiException(String message) throws BusiException {
        throw new BusiException(message);
    }

    public static void isTrue(boolean expression, String message) throws BusiException {
        if (!expression) {
            throw new BusiException(message);
        }
    }

    public static void isTrue(boolean expression) throws BusiException {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isNull(Object object, String message) throws BusiException {
        if (object != null) {
            throw new BusiException(message);
        }
    }

    public static void isNull(Object object) throws BusiException {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) throws BusiException {
        if (object == null) {
            throw new BusiException(message);
        }
    }

    public static void notNull(Object object) throws BusiException {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void hasLength(String text, String message) throws BusiException {
        if (!StringUtils.hasLength(text)) {
            throw new BusiException(message);
        }
    }

    public static void hasLength(String text) throws BusiException {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static void hasText(String text, String message) throws BusiException {
        if (!StringUtils.hasText(text)) {
            throw new BusiException(message);
        }
    }

    public static void hasText(String text) throws BusiException {
        hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    public static void doesNotContain(String textToSearch, String substring, String message) throws BusiException {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new BusiException(message);
        }
    }

    public static void doesNotContain(String textToSearch, String substring) throws BusiException {
        doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
    }

    public static void notEmpty(Object[] array, String message) throws BusiException {
        if (ObjectUtils.isEmpty(array)) {
            throw new BusiException(message);
        }
    }

    public static void notEmpty(Object[] array) throws BusiException {
        notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    public static void noNullElements(Object[] array, String message) throws BusiException {
        if (array != null) {
            Object[] var2 = array;
            int var3 = array.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Object element = var2[var4];
                if (element == null) {
                    throw new BusiException(message);
                }
            }
        }

    }

    public static void noNullElements(Object[] array) throws BusiException {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    public static void notEmpty(Collection<?> collection, String message) throws BusiException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusiException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) throws BusiException {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Map<?, ?> map, String message) throws BusiException {
        if (CollectionUtils.isEmpty(map)) {
            throw new BusiException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map) throws BusiException {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    public static void isInstanceOf(Class<?> clazz, Object obj) throws BusiException {
        isInstanceOf(clazz, obj, "");
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) throws BusiException {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new BusiException((StringUtils.hasLength(message) ? message + " " : "") + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) throws BusiException {
        isAssignable(superType, subType, "");
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) throws BusiException {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new BusiException(message + subType + " is not assignable to " + superType);
        }
    }

    public static void state(boolean expression, String message) throws BusiException {
        if (!expression) {
            throw new BusiException(message);
        }
    }

    public static void state(boolean expression) throws BusiException {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }
}
