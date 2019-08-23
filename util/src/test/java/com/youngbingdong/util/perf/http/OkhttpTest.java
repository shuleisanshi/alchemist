package com.youngbingdong.util.perf.http;

import okhttp3.Headers.Builder;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import static com.youngbingdong.util.http.OkHttpUtil.getHttpClient;

/**
 * @author ybd
 * @date 2019/8/22
 * @contact yangbingdong1994@gmail.com
 */
public class OkhttpTest {

    //language=JSON
    private static String body = "{\"time\":{\"__op\":\"Increment\",\"amount\":200},\"_method\":\"PUT\",\"_ApplicationId\":\"usE0s6JGUeOiMcsVoRuHuv2B-gzGzoHsz\",\"_ApplicationKey\":\"ewm6NEF07r83HbnOr63ptuKH\",\"_ClientVersion\":\"js0.6.1\",\"_InstallationId\":\"e42f2903-1dcd-5f15-c11d-c582c4305c5f\"}";

    public static void main(String[] args) {
        Builder headerBuilder = new Builder();
        headerBuilder.add("Content-Type", "text/plain")
                     .add("Referer", "https://yangbingdong.com/2019/nexus3-by-docker/")
                     .add("Sec-Fetch-Mode", "cors")
                     .add("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), body);
        Request build = new Request.Builder().post(requestBody)
                                             .url("https://api.leancloud.cn/1.1/classes/Counter/5be10b4b7565710068c2e3b4?new=true")
                                             .headers(headerBuilder.build())
                                             .build();
        try (Response response = getHttpClient().newCall(build).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println("===========> " + response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
