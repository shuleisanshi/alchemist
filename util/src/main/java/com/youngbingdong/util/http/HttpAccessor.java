package com.youngbingdong.util.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.youngbingdong.util.http.ApiRequest.HTTP_METHOD_GET;

/**
 * @author ybd
 * @date 2019/8/15
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class HttpAccessor {

    private static OkHttpClient okHttpClient;

    static final int DEFAULT_TIMEOUT = 8;

    static {
        Builder builder = new Builder().readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder = configureToIgnoreCertificate(builder);
        okHttpClient = builder.build();
        Dispatcher dispatcher = okHttpClient.dispatcher();
        dispatcher.setMaxRequests(1 << 8);
        dispatcher.setMaxRequestsPerHost(1 << 6);
    }

    public static OkHttpClient getHttpClient() {
        return okHttpClient;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static <R> R send(ApiRequest<R> apiRequest) {
        try {
            OkHttpClient client = getOkHttpClient(apiRequest);
            Request okHttpRequest = createRequest(apiRequest);
            Response response = client.newCall(okHttpRequest).execute();
            String body = response.body().string();
            if (String.class.equals(apiRequest.getRespClass())) {
                return (R) body;
            }
            if (JSONObject.class.equals(apiRequest.getRespClass())) {
                return (R) JSONObject.parseObject(body);
            }
            return JSONObject.parseObject(body, apiRequest.getRespClass());
        } catch (IOException e) {
            throw new HttpAccessException(e);
        }
    }

    private static <R> OkHttpClient getOkHttpClient(ApiRequest<R> apiRequest) {
        if (DEFAULT_TIMEOUT < apiRequest.getTimeout()) {
            return okHttpClient.newBuilder()
                               .readTimeout(apiRequest.getTimeout(), TimeUnit.SECONDS)
                               .build();
        }
        return okHttpClient;
    }

    private static <R> Request createRequest(ApiRequest<R> request) {
        Request.Builder builder = new Request.Builder();
        if (HTTP_METHOD_GET.equalsIgnoreCase(request.getHttpMethod())) {
            if (request.getParameters() != null && request.getParameters().size() > 0) {
                Set<String> keys = request.getParameters().keySet();
                StringBuilder sb = new StringBuilder(request.getUrl());
                sb.append(request.getUrl().contains("=") ? "&" : "?");
                for (String key : keys) {
                    sb.append(key).append('=').append(request.getParameters().get(key)).append('&');
                }
                request.url(sb.substring(0, sb.length() - 1));
            }
        } else {
            builder.method(request.getHttpMethod(), createRequestBody(request));
        }
        builder.url(request.getUrl());
        if (log.isDebugEnabled()) {
            log.debug("OkHttp Request Url: {}", request.getUrl());
        }
        if (null != request.getHeaderBuilder()) {
            builder.headers(request.getHeaderBuilder().build());
        }
        return builder.build();
    }

    private static <R> RequestBody createRequestBody(ApiRequest<R> request) {
        if (request.isFormBody()) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
                builder.add(parameter.getKey(), String.valueOf(parameter.getValue()));
            }
            return builder.build();
        } else {
            String json = toJSONString(request.getParameters());
            if (log.isDebugEnabled()) {
                log.debug("OkHttp Request Body: {}", json);
            }
            return RequestBody.create(request.getMediaType(), json);
        }
    }

    /**
     * Setting testMode configuration. If set as testMode, the connection will skip certification check
     */
    public static Builder configureToIgnoreCertificate(Builder builder) {
        try {
            /*Create a trust manager that does not validate certificate chains*/
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            /*Install the all-trusting trust manager*/
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            /*Create an ssl socket factory with our all-trusting manager*/
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            log.warn("Exception while configuring IgnoreSslCertificate" + e, e);
        }
        return builder;
    }
}
