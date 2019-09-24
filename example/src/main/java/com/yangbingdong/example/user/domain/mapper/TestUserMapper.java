package com.yangbingdong.example.user.domain.mapper;

import com.yangbingdong.example.user.domain.entity.TestUser;
import com.yangbingdong.service.core.CustomBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yangbingdong
 * @since 2019-09-09
 */
@Mapper
public interface TestUserMapper extends CustomBaseMapper<TestUser> {
    /* !!!TUPLE_MARK_START!!! */
    /* !!!TUPLE_MARK_END!!! */
}
