package com.youngbingdong.util.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * @author ybd
 * @date 2019/8/15
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class OkHttpUtil {

    private static OkHttpClient okHttpClient;

    static {
        Builder builder = new Builder().connectTimeout(5, TimeUnit.SECONDS)
                                       .readTimeout(5, TimeUnit.SECONDS)
                                       .writeTimeout(5, TimeUnit.SECONDS);
        builder = configureToIgnoreCertificate(builder);
        okHttpClient = builder.build();
    }

    public static String get(String uri) {
        try (Response response = okHttpClient.newCall(new Request.Builder().get()
                                                                           .url(uri)
                                                                           .build()).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
