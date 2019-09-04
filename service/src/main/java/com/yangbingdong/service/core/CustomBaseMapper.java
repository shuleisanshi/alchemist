package com.yangbingdong.service.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
public interface CustomBaseMapper<T> extends BaseMapper<T> {
    Long selectMaxId();
}
