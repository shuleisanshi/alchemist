package com.yangbingdong.service.mp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yangbingdong.service.mp.enums.AgeEnum;
import com.yangbingdong.service.util.FastJsonEnumCodec;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class User {
    private Long id;

    private String name;

    private String email;

    /**
     * IEnum接口的枚举处理
     */
    @JSONField(serializeUsing = FastJsonEnumCodec.class, deserializeUsing = FastJsonEnumCodec.class)
    private AgeEnum age;

    @TableLogic
    private boolean deleted;

}
