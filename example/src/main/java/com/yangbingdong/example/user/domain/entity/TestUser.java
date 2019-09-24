package com.yangbingdong.example.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.youngbingdong.redisoper.core.metadata.annotation.RedisIndex;
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

    /* !!!TUPLE_MARK_START!!! */
    private static final long serialVersionUID = 1L;

    public static final String IDX_UK = "IDX_UK";
    public static final String IDX_NAME = "IDX_NAME";
    public static final String IDX_UNION = "IDX_UNION";

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    @RedisPrimaryKey
    private Long id;

    /**
     * 姓名
     */
    @TableField("Name")
    @RedisIndex(name = IDX_NAME)
    private String Name;

    /**
     * 年龄
     */
    @RedisIndex(name = IDX_UNION)
    private Integer age;

    /**
     * 邮箱
     */
    @RedisIndex(name = IDX_UNION, order = 1)
    private String email;

    @TableLogic
    private Boolean deleted;

    @RedisIndex(name = IDX_UK, unique = true)
    private Long testUk;


    public static final String ID = "id";

    public static final String NAME = "Name";

    public static final String AGE = "age";

    public static final String EMAIL = "email";

    public static final String DELETED = "deleted";

    public static final String TEST_UK = "test_uk";

    /* !!!TUPLE_MARK_END!!! */

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
