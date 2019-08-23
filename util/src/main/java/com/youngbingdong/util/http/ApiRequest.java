package com.youngbingdong.util.http;

import com.youngbingdong.util.reflect.BeanUtil;
import lombok.Getter;
import okhttp3.Headers;
import okhttp3.MediaType;

import java.util.HashMap;
import java.util.Map;

import static com.youngbingdong.util.http.HttpAccessor.DEFAULT_TIMEOUT;

/**
 * @author ybd
 * @date 2019/8/23
 * @contact yangbingdong1994@gmail.com
 */
@Getter
public class ApiRequest<R> {

    private static final MediaType MEDIA_TYPE__JSON = MediaType.parse("application/json");
    private static final MediaType MEDIA_TYPE__FORM = MediaType.parse("application/x-www-form-urlencoded");

    public static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    private String url;
    private String httpMethod = HTTP_METHOD_GET;
    private MediaType mediaType;
    private int timeout = DEFAULT_TIMEOUT;
    private boolean formBody = false;
    private Map<String, Object> parameters;
    private Headers.Builder headerBuilder;

    private Class<R> respClass;

    public static ApiRequest<String> newRequest(String url) {
        return new ApiRequest<>(url, String.class);
    }

    public static <R> ApiRequest<R> newRequest(String url, Class<R> clazz) {
        return new ApiRequest<>(url, clazz);
    }

    public ApiRequest(String url, Class<R> respClass) {
        this.url = url;
        this.respClass = respClass;
    }

    public ApiRequest<R> header(String k, String v) {
        if (this.headerBuilder == null) {
            headerBuilder = new Headers.Builder();
        }
        headerBuilder.add(k, v);
        return this;
    }

    public ApiRequest<R> post() {
        this.httpMethod = HTTP_METHOD_POST;
        this.mediaType = MEDIA_TYPE__JSON;
        return this;
    }

    public ApiRequest<R> formBody() {
        this.formBody = true;
        this.mediaType = MEDIA_TYPE__FORM;
        return this;
    }

    public ApiRequest<R> parameter(String k, Object v) {
        if (parameters == null) {
            parameters = new HashMap<>(16);
        }
        parameters.put(k, v);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ApiRequest<R> parameter(Object parameter) {
        if (parameters == null) {
            parameters = new HashMap<>(16);
        }
        if (parameter instanceof Map) {
            parameters.putAll((Map<? extends String, ?>) parameter);
        } else {
            parameters.putAll(BeanUtil.toMap(parameter));
        }
        return this;
    }

    public ApiRequest<R> url(String url) {
        this.url = url;
        return this;
    }

    public ApiRequest<R> mediaType(String mediaType) {
        this.mediaType = MediaType.parse(mediaType);
        return this;
    }

}
