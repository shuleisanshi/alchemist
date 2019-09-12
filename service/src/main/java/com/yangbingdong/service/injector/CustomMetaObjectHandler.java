package com.yangbingdong.service.injector;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

import static com.youngbingdong.util.time.SystemTimer.nowDateTime;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
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
