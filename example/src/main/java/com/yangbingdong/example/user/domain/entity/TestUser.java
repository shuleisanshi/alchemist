package com.yangbingdong.example.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.youngbingdong.redisoper.core.metadata.annotation.RedisPrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangbingdong
 * @since 2019-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TestUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @RedisPrimaryKey
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 姓名
     */
    @TableField("Name")
    private String Name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    @TableLogic
    private Boolean deleted;

    private Long testUk;


    public static final String ID = "id";

    public static final String NAME = "Name";

    public static final String AGE = "age";

    public static final String EMAIL = "email";

    public static final String DELETED = "deleted";

    public static final String TEST_UK = "test_uk";

}
