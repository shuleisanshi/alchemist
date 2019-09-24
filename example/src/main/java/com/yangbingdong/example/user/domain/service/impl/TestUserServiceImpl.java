package com.yangbingdong.example.user.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangbingdong.example.user.domain.entity.TestUser;
import com.yangbingdong.example.user.domain.mapper.TestUserMapper;
import com.yangbingdong.example.user.domain.service.TestUserService;
import com.yangbingdong.service.core.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangbingdong
 * @since 2019-09-09
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements TestUserService {
    /* !!!TUPLE_MARK_START!!! */

    @Override
    public TestUser getById(Long id) {
        return redisoper.getByKey(() -> baseMapper.selectById(id), id);
    }

    @Override
    public TestUser getByTestUk(Long testUk) {
        return redisoper.getByUniqueIndex(() -> getOne(new QueryWrapper<TestUser>().eq(TestUser.TEST_UK, testUk)), TestUser.IDX_UK, testUk);
    }

    @Override
    public List<TestUser> getByName(String Name) {
        return redisoper.getByNormalIndex(() -> list(new QueryWrapper<TestUser>().eq(TestUser.NAME, Name)), TestUser.IDX_NAME, Name);
    }

    @Override
    public List<TestUser> getByAgeAndEmail(Integer age ,String email) {
        return redisoper.getByNormalIndex(() -> list(new QueryWrapper<TestUser>().eq(TestUser.AGE, age).eq(TestUser.EMAIL, email)), TestUser.IDX_UNION, age ,email);
    }

    /* !!!TUPLE_MARK_END!!! */
}
