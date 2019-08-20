package com.youngbingdong.redisoper.extend.geo.core;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs;

/**
 * @author ybd
 * @date 2019/8/16
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class RedisGeoHelper {

    private final GeoOperations<String, String> opsForGeo;

    public RedisGeoHelper(StringRedisTemplate redisTemplate) {
        this.opsForGeo = redisTemplate.opsForGeo();
    }

    /**
     * 添加经纬度信息
     */
    public Long add(String key, Point point, String member) {
        return opsForGeo.add(key, point, member);
    }

    /**
     * 批量添加经纬度信息
     */
    public Long add(String key, Map<String, Point> pointMap) {
        return opsForGeo.add(key, pointMap);
    }

    /**
     * 查找指定key的经纬度信息，可以指定多个key，批量返回
     */
    public List<Point> position(String key, String... member) {
        return opsForGeo.position(key, member);
    }

    /**
     * 删除经纬度信息
     */
    public Long remove(String key, String... member) {
        return opsForGeo.remove(key, member);
    }

    /**
     * 获取两个地点之间的距离
     */
    public Double distance(String key, String member1, String member2) {
        return requireNonNull(opsForGeo.distance(key, member1, member2)).getValue();
    }

    /**
     * 返回一个或多个位置元素的 Geohash 表示
     */
    public List<String> hash(String key, String... member) {
        return opsForGeo.hash(key, member);
    }


    /**
     * 根据给定的经纬度，返回半径不超过指定距离的元素,时间复杂度为O(N+log(M))，N为指定半径范围内的元素个数，M为要返回的个数
     * redis命令：georadius cityGeo 116.405285 39.904989 100 km WITHDIST WITHCOORD ASC COUNT 5
     */
    public List<GeoRadiusResult> radius(String key, Point point, double radius, int limit) {
        Circle circle = new Circle(point, radius);
        GeoRadiusCommandArgs geoRadiusArgs = newGeoRadiusArgs().includeCoordinates().includeDistance()
                                                               .sortAscending()
                                                               .limit(limit);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = opsForGeo.radius(key, circle, geoRadiusArgs);
        if (geoResults == null) {
            return emptyList();
        }
        return geoResults.getContent()
                         .stream()
                         .map(GeoRadiusResult::translate)
                         .collect(toList());
    }

    public List<GeoRadiusResult> radius(String key, Point point, double radius) {
        return radius(key, point, radius, 100);
    }

}
