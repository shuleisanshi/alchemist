package com.yangbingdong.service.mp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangbingdong.service.mp.entity.User;
import com.yangbingdong.service.mp.mapper.UserMapper;
import com.yangbingdong.service.mp.model.MyPage;
import com.yangbingdong.service.mp.model.ParamSome;
import com.yangbingdong.service.mp.model.UserChildren;
import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PaginationTest {

    @Resource
    private UserMapper mapper;

    @Test
    public void lambdaPagination() {
        Page<User> page = new Page<>(1, 3);
        page.setOptimizeCountSql(true).setSearchCount(true);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().ge(User::getAge, 0).orderByAsc(User::getAge);
        IPage<User> result = mapper.selectPage(page, wrapper);
        System.out.println(result.getTotal());
        Assert.assertTrue(result.getTotal() > 3);
        Assert.assertEquals(3, result.getRecords().size());
    }

    @Test
    public void tests1() {
        System.out.println("----- baseMapper 自带分页 ------");
        Page<User> page = new Page<>(2, 5);
        IPage<User> userIPage = mapper.selectPage(page, new QueryWrapper<User>().eq("age", 1).eq("name", "Jack"));
        assertThat(page).isSameAs(userIPage);
        System.out.println("总条数 ------> " + userIPage.getTotal());
        System.out.println("当前页数 ------> " + userIPage.getCurrent());
        System.out.println("当前每页显示数 ------> " + userIPage.getSize());
        print(userIPage.getRecords());
        System.out.println("----- baseMapper 自带分页 ------");

        System.out.println("json 正反序列化 begin");
        String json = JSON.toJSONString(page);
        Page<User> page1 = JSON.parseObject(json, new TypeReference<Page<User>>(){});
        print(page1.getRecords());
        System.out.println("json 正反序列化 end");

        System.out.println("----- 自定义 XML 分页 ------");
        MyPage<User> myPage = new MyPage<User>(3, 5).setSelectInt(1).setSelectStr("Jack");
        ParamSome paramSome = new ParamSome(20, "Jack");
        MyPage<User> userMyPage = mapper.mySelectPage(myPage, paramSome);
        assertThat(myPage).isSameAs(userMyPage);
        System.out.println("总条数 ------> " + userMyPage.getTotal());
        System.out.println("当前页数 ------> " + userMyPage.getCurrent());
        System.out.println("当前每页显示数 ------> " + userMyPage.getSize());
        print(userMyPage.getRecords());
        System.out.println("----- 自定义 XML 分页 ------");
    }

    @Test
    public void tests2() {
        /* 下面的 left join 不会对 count 进行优化,因为 where 条件里有 join 的表的条件 */
        MyPage<UserChildren> myPage = new MyPage<>(1, 5);
        myPage.setSelectInt(18).setSelectStr("Jack");
        MyPage<UserChildren> userChildrenMyPage = mapper.userChildrenPage(myPage);
        List<UserChildren> records = userChildrenMyPage.getRecords();
        records.forEach(System.out::println);

        /* 下面的 left join 会对 count 进行优化,因为 where 条件里没有 join 的表的条件 */
        myPage = new MyPage<UserChildren>(1, 5).setSelectInt(1);
        userChildrenMyPage = mapper.userChildrenPage(myPage);
        records = userChildrenMyPage.getRecords();
        records.forEach(System.out::println);
    }

    private <T> void print(List<T> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(System.out::println);
        }
    }


    @Test
    public void testMyPageMap() {
        MyPage<User> myPage = new MyPage<User>(1, 5);
        Map<String, String> map = new HashMap<>(1);
        map.put("name", "%a%");
        MyPage<User> userMyPage = mapper.mySelectPageMap(myPage, map);
        myPage.getRecords().forEach(System.out::println);
        userMyPage.getRecords().forEach(System.out::println);
    }

    @Test
    public void testMap() {
        Map<String, String> map = new HashMap<>(1);
        map.put("name", "%a");
        mapper.mySelectMap(map).forEach(System.out::println);
    }

    @Test
    public void myPage() {
        MyPage<User> page = new MyPage<>(1, 5);
        page.setName("a");
        mapper.myPageSelect(page).forEach(System.out::println);
    }

    @Test
    public void iPageTest() {
        IPage<User> page = new Page<User>(1, 5) {
            private static final long serialVersionUID = 2852617767840949414L;
            private String name = "%";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        };

        List<User> list = mapper.iPageSelect(page);
        System.out.println("list.size=" + list.size());
        System.out.println("page.total=" + page.getTotal());
    }

    @Test
    public void rowBoundsTest() {
        RowBounds rowBounds = new RowBounds(0, 5);
        Map map = new HashMap(1);
        map.put("name", "%");
        List<User> list = mapper.rowBoundList(rowBounds, map);
        System.out.println("list.size=" + list.size());
    }
}
