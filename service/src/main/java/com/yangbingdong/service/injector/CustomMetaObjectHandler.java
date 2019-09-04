package com.yangbingdong.service.injector;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

import static com.youngbingdong.util.time.SystemTimer.nowDateTime;

/**
 * 填充器
 *
 * @author nieqiurong 2018-08-10 22:59:23.
 */
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime fieldVal = nowDateTime();
        setFieldValByName("createTime", fieldVal, metaObject);
        setFieldValByName("updateTime", fieldVal, metaObject);
        setFieldValByName("deleted", Boolean.FALSE, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", nowDateTime(), metaObject);
    }
}
