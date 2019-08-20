package com.youngbingdong.redisoper.extend.geo.core;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;

/**
 * @author ybd
 * @date 2019/8/16
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class GeoRadiusResult {
    private Double distance;

    private String name;

    private Double lng;

    private Double lat;

    public static GeoRadiusResult translate(GeoResult<GeoLocation<String>> geoLocationGeoResult) {
        GeoRadiusResult geoRadiusResult = new GeoRadiusResult();
        GeoLocation<String> content = geoLocationGeoResult.getContent();
        Point point = content.getPoint();
        geoRadiusResult.setDistance(geoLocationGeoResult.getDistance().getValue())
                       .setLng(point.getX())
                       .setLat(point.getY())
                       .setName(content.getName());
        return geoRadiusResult;
    }
}
