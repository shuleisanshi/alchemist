package com.youngbingdong.util.perf.http;

import com.alibaba.fastjson.JSONObject;
import com.youngbingdong.util.http.ApiRequest;
import com.youngbingdong.util.http.HttpAccessor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author ybd
 * @date 2019/8/22
 * @contact yangbingdong1994@gmail.com
 */
public class HttpAccessorTest {

    @Test
    public void accessTest() {
        //language=JSON
        String body = "{\"time\":{\"__op\":\"Increment\",\"amount\":1},\"_method\":\"PUT\",\"_ApplicationId\":\"usE0s6JGUeOiMcsVoRuHuv2B-gzGzoHsz\",\"_ApplicationKey\":\"ewm6NEF07r83HbnOr63ptuKH\",\"_ClientVersion\":\"js0.6.1\",\"_InstallationId\":\"e42f2903-1dcd-5f15-c11d-c582c4305c5f\"}";
        ApiRequest<JSONObject> request = ApiRequest.newRequest("https://api.leancloud.cn/1.1/classes/Counter/5be10b4b7565710068c2e3b4?new=true", JSONObject.class);

        request.post()
               .parameter(parseObject(body).getInnerMap())
               .mediaType("text/plain");

        request.header("Referer", "https://yangbingdong.com/2019/nexus3-by-docker/")
               .header("Sec-Fetch-Mode", "cors")
               .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");

        JSONObject result = HttpAccessor.access(request);
        System.out.println(result);
    }

    @Test
    public void accessCallbackTest() throws InterruptedException {
        ApiRequest<String> request = ApiRequest.newRequest("http://ip.gs");
        CountDownLatch latch = new CountDownLatch(1);
        HttpAccessor.access(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(call.timeout());
                System.out.println(response.body().string());
                latch.countDown();
            }
        });
        latch.await();
    }
}
