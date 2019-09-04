package com.yangbingdong.service.mp.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
public enum GenderEnum implements IEnum<Integer> {
    MALE(0, "男"),
    FEMALE(1, "女");

    private int value;
    private String desc;

    GenderEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }


    @Override
    public Integer getValue() {
        return value;
    }
}
