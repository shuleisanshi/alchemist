package com.youngbingdong.redisoper.extend.geo.locate;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.youngbingdong.redisoper.extend.geo.config.LocatorProperty;
import com.youngbingdong.util.http.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.requireNonNull;

/**
 * @author ybd
 * @date 2019/8/16
 * @contact yangbingdong1994@gmail.com
 * <p>
 * https://lbs.amap.com/api/webservice/guide/api/georegeo
 */
@Slf4j
public class GaodeLocator implements Locator {

    private static final String URL = "https://restapi.amap.com/v3/geocode/geo?";
    private static final String PARAM = "address=%s&key=%s";

    private String key;
    private String sign;

    public GaodeLocator(LocatorProperty locatorProperty) {
        this.key = requireNonNull(locatorProperty.getKey());
        this.sign = requireNonNull(locatorProperty.getSign());
    }

    @Override
    public LngLatPair locate(String address) {
        String realParam = String.format(PARAM, address, key);
        String signMd5 = SecureUtil.md5(realParam + sign);
        String body = OkHttpUtil.get(URL + realParam + "&sig=" + signMd5);
        JSONObject json = JSONObject.parseObject(body);
        String status = (String) json.getOrDefault("status", "-1");
        if (!"1".equals(status)) {
            log.error("获取失败 -> " + address);
            throw new IllegalStateException("坐标获取失败");
        }
        String locationString = ((JSONObject) json.getJSONArray("geocodes").get(0)).getString("location");
        String[] location = locationString.split(",");
        return new LngLatPair().setLng(Double.valueOf(location[0]))
                               .setLat(Double.valueOf(location[1]));
    }
}
