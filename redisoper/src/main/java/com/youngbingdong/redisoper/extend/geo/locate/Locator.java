package com.youngbingdong.redisoper.extend.geo.locate;

/**
 * @author ybd
 * @date 2019/8/16
 * @contact yangbingdong1994@gmail.com
 */
public interface Locator {

    /**
     * 获取坐标
     * @param address 详细地址
     * @return 坐标
     */
    LngLatPair locate(String address);
}
