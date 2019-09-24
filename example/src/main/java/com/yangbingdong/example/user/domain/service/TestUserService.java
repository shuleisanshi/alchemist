package com.yangbingdong.example.user.domain.service;

import com.yangbingdong.example.user.domain.entity.TestUser;
import com.yangbingdong.service.core.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangbingdong
 * @since 2019-09-09
 */
public interface TestUserService extends Service<TestUser> {
    /* !!!TUPLE_MARK_START!!! */
    TestUser getById(Long id);

    TestUser getByTestUk(Long testUk);

    List<TestUser> getByName(String Name);

    List<TestUser> getByAgeAndEmail(Integer age ,String email);

    /* !!!TUPLE_MARK_END!!! */
}
