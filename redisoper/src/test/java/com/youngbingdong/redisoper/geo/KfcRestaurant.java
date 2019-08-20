package com.youngbingdong.redisoper.geo;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.geo.Point;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;
import static java.lang.String.format;

/**
 * @author ybd
 * @date 2019/8/15
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class KfcRestaurant {

    @CsvBindByName(column = "id")
    private Long id;

    private String name;

    @CsvBindByName(column = "name")
    private String address;

    private Double lng;

    private Double lat;

    public static KfcRestaurant fromJson(String json) {
        return parseObject(json, KfcRestaurant.class);
    }

    public static String getRedisKey(Long id) {
        return format(TestConstants.KFC_KEY, KfcRestaurant.class.getSimpleName(), id);
    }

    public static Long resolveIdFromMember(String member) {
        return Long.valueOf(member.substring(member.indexOf(":") + 1));
    }

    public String toJson() {
        return toJSONString(this);
    }

    public String getRedisKey() {
        return getRedisKey(id);
    }

    public Point obtainPoint() {
        return new Point(lng, lat);
    }

    public String toPointMember() {
        return name + ":" + id;
    }

}
