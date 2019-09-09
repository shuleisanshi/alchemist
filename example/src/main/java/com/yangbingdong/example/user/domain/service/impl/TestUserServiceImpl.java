package com.yangbingdong.example.user.domain.service.impl;

import com.yangbingdong.example.user.domain.entity.TestUser;
import com.yangbingdong.example.user.domain.mapper.TestUserMapper;
import com.yangbingdong.example.user.domain.service.TestUserService;
import com.yangbingdong.service.core.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
