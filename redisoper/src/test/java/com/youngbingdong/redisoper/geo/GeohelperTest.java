package com.youngbingdong.redisoper.geo;

import com.youngbingdong.redisoper.RedisoperApplicationTests;
import com.youngbingdong.redisoper.extend.geo.core.GeoRadiusResult;
import com.youngbingdong.redisoper.extend.geo.core.RedisGeoHelper;
import com.youngbingdong.redisoper.extend.geo.locate.LngLatPair;
import com.youngbingdong.redisoper.extend.geo.locate.Locator;
import com.youngbingdong.util.excel.CvsUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.youngbingdong.redisoper.geo.TestConstants.GEO_KEY;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class GeohelperTest extends RedisoperApplicationTests {

    private static final String MEMBER = "广东省广州市丰兴广场A坐";

    private static final String KEY = "GEO_HELPER:TEST";

    @Autowired
    private Locator locator;

    @Autowired
    private RedisGeoHelper redisGeoHelper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @SuppressWarnings("ConstantConditions")
    @Test
    @Ignore
    public void initKfcData() {
        if (redisTemplate.hasKey(GEO_KEY)) {
            return;
        }
        List<KfcRestaurant> kcf = CvsUtil.readVsvToBean("kfc.csv", KfcRestaurant.class);
        requireNonNull(kcf);
        List<KfcRestaurant> kfcList = kcf.stream()
                                         .peek(this::mapToKfcBean)
                                         .filter(Objects::nonNull)
                                         .collect(toList());
        Map<String, String> map = kfcList.stream()
                                         .collect(toMap(KfcRestaurant::getRedisKey, KfcRestaurant::toJson));

        redisTemplate.opsForValue()
                     .multiSet(map);

        Map<String, Point> pointMap = kfcList.stream()
                                             .collect(toMap(KfcRestaurant::toPointMember, KfcRestaurant::obtainPoint));
        Long result = redisGeoHelper.add(GEO_KEY, pointMap);
        log.info("插入地图成功, 条数: {}", result);
    }

    private void mapToKfcBean(KfcRestaurant kfc) {
        kfc.setName(kfc.getAddress());
        fillLongAndLat(kfc);
    }

    private void fillLongAndLat(KfcRestaurant kfcRestaurant) {
        LngLatPair locate = locator.locate(kfcRestaurant.getAddress());
        if (locate.getLng() == null || locate.getLat() == null) {
            log.info("=========================> null");
        }
        kfcRestaurant.setLng(locate.getLng())
                     .setLat(locate.getLat());
        try {
            Thread.sleep(350L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 广东省广州市丰兴广场A坐: lng=113.33316, lat=23.13295
     */
    @Test
    public void locatorTest() {
        LngLatPair lngLatPair = locator.locate(MEMBER);
        log.info("{}", lngLatPair);
        Assertions.assertThat(lngLatPair)
                  .isNotNull();
    }

    @Test
    @Ignore
    public void radiusTest() {
        List<GeoRadiusResult> geoRadiusResults = redisGeoHelper.radius(GEO_KEY, new Point(113.33316, 23.13295), 1500);
        geoRadiusResults.forEach(e -> log.info("{}", e));
    }

    @Test
    public void addAndRemoveGeoTest() {
        Long result = redisGeoHelper.add(KEY, new Point(113.33316, 23.13295), MEMBER);
        Assertions.assertThat(result)
                  .isEqualTo(1L);
        Long remove = redisGeoHelper.remove(KEY, MEMBER);
        Assertions.assertThat(remove)
                  .isEqualTo(1L);
    }

    @Test
    public void positionTest() {
        Point point = new Point(113.33316, 23.13295);
        redisGeoHelper.add(KEY, point, MEMBER);
        List<Point> position = redisGeoHelper.position(KEY, MEMBER);
        Assertions.assertThat(position)
                  .hasSize(1);
        redisGeoHelper.remove(KEY, MEMBER);
    }

    @Test
    public void distanceTest() {
        Point point1 = new Point(113.33316, 23.13295);
        Point point2 = new Point(114.33316, 23.13295);
        redisGeoHelper.add(KEY, point1, "distance1");
        redisGeoHelper.add(KEY, point2, "distance2");

        Double distance = redisGeoHelper.distance(KEY, "distance1", "distance2");
        log.info("{}", distance);
        Assertions.assertThat(distance)
                  .isNotNull();
        redisGeoHelper.remove(KEY, "distance1", "distance2");
    }

    @Test
    public void hashTest() {
        Point point = new Point(113.33316, 23.13295);
        redisGeoHelper.add(KEY, point, MEMBER);
        List<String> hash = redisGeoHelper.hash(KEY, MEMBER);
        Assertions.assertThat(hash.get(0))
                  .isNotNull();
        log.info("GeoHash ==> {}", hash.get(0));
        redisGeoHelper.remove(KEY, MEMBER);
    }

}
