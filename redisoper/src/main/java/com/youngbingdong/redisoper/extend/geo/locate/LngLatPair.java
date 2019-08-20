package com.youngbingdong.redisoper.extend.geo.locate;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 2019/8/16
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class LngLatPair {
    private Double lng;

    private Double lat;
}
