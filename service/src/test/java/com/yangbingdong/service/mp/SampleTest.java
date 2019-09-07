package com.yangbingdong.service.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangbingdong.service.mp.entity.User;
import com.yangbingdong.service.mp.enums.AgeEnum;
import com.yangbingdong.service.mp.mapper.UserMapper;
import com.yangbingdong.service.util.Wrappers;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static com.yangbingdong.service.util.Wrappers.query;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Resource
    private UserMapper mapper;

    @Test
    public void insert() {
        User user = new User();
        user.setId(666L)
            .setName("D神")
            .setAge(AgeEnum.ONE)
            .setEmail("yangbingdong1994@gmail.com");

        Assert.assertTrue(mapper.insert(user) > 0);
        System.err.println("插入成功 ID 为：" + user.getId());
        List<User> list = mapper.selectList(Wrappers.emptyWrapper());
        for (User u : list) {
            System.out.println(u);
            Assert.assertNotNull("age should not be null", u.getAge());
        }
    }

    @Test
    public void delete() {
        Assert.assertTrue(mapper.delete(new QueryWrapper<User>()
                .lambda().eq(User::getAge, AgeEnum.TWO)) > 0);
    }

    @Test
    public void update() {
        Assert.assertTrue(mapper.update(new User().setAge(AgeEnum.TWO),
                new QueryWrapper<User>().eq("age", AgeEnum.THREE)) > 0);
    }

    // @Transactional
    @Test
    public void select() {
        User user = mapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId, 2));
        Assert.assertEquals("Jack", user.getName());
    }

    @Test
    public void selectMaxId() {
        Long maxId = mapper.selectMaxId();
        System.out.println("maxId: " + maxId);
        Assertions.assertThat(maxId)
                  .isGreaterThan(0);
    }

    @Test
    public void tableLogicDeleted() {
        int i = mapper.deleteById(3);
        System.out.println(i);
        User user = mapper.selectById(3);
        System.out.println(user);

        query(User.class)
                .eq(false, null, null);
    }
}
