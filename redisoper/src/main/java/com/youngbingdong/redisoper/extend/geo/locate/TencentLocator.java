package com.youngbingdong.redisoper.extend.geo.locate;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.youngbingdong.redisoper.extend.geo.config.LocatorProperty;
import com.youngbingdong.util.http.ApiRequest;
import com.youngbingdong.util.http.HttpAccessor;
import lombok.extern.slf4j.Slf4j;

import static com.youngbingdong.util.http.ApiRequest.newRequest;
import static java.util.Objects.requireNonNull;

/**
 * @author ybd
 * @date 2019/8/15
 * @contact yangbingdong1994@gmail.com
 *
 * https://lbs.qq.com/webservice_v1/guide-geocoder.html
 */
@Slf4j
public class TencentLocator implements Locator {
    private static final String HOST = "https://apis.map.qq.com";
    private static final String URL = "/ws/geocoder/v1/?address=%s&key=%s";

    private String key;
    private String sign;

    public TencentLocator(LocatorProperty locatorProperty) {
        this.key = requireNonNull(locatorProperty.getKey());
        this.sign = requireNonNull(locatorProperty.getSign());
    }

    @Override
    public LngLatPair locate(String address) {
        String realUrl = String.format(URL, address, key);
        String signMd5 = SecureUtil.md5(realUrl + sign);
        ApiRequest<JSONObject> request = newRequest(HOST + realUrl + "&sig=" + signMd5, JSONObject.class);
        JSONObject json = HttpAccessor.send(request);
        Integer status = (Integer) json.getOrDefault("status", -1);
        if (status != 0) {
            log.error("获取失败 -> " + address);
            throw new IllegalStateException("坐标获取失败");
        }
        return json.getJSONObject("result").getObject("location", LngLatPair.class);
    }
}
