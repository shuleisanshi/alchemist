# MyBatis-Plus 使用简单说明

> 详细配置说明: ***[https://mp.baomidou.com/config](https://mp.baomidou.com/config)***

配置文件:
```
mybatis-plus:
  # 告诉 Mapper 所对应的 XML 文件位置
  mapper-locations: classpath:/mapper/*Mapper.xml
  global-config:
    db-config:
      id-type: input
  configuration:
    # 关闭缓存
    cache-enabled: false
```

一些默认规则:

* 字段以及表名使用默认使用驼峰模式, 对应的配置 `mapUnderscoreToCamelCase`

## 扫包配置

可在配置类上加上 `@MapperScan("com.yangbingdong.*.mapper")` 或者在 Mapper 接口上 `@Mapper` 注解.

## 枚举

配置枚举包扫描:

```
mybatis-plus:
  type-enums-package: com.yangbingdong.service.mp.enums
```

枚举类实现 `IEnum<T>` 接口:

```java
public enum AgeEnum implements IEnum<Integer> {
  ONE(1, "一岁"),
  TWO(2, "二岁"),
  THREE(3, "三岁");

  private int value;
  private String desc;

  AgeEnum(final int value, final String desc) {
    this.value = value;
    this.desc = desc;
  }

  @Override
  public Integer getValue() {
    return value;
  }
}
```

## 基本查询

大部分普通查询通用 Mapper 可满足:

```java
Wrapper<User> wrapper = new QueryWrapper<>();
wrapper.lambda().ge(User::getAge, 1).orderByAsc(User::getAge);
List<User> users = mapper.selectList(wrapper);
```

嵌套查询:

```java
new QueryWrapper<User>().lambda()
                        .nested(i -> i.eq(User::getRoleId, 2L).or().eq(User::getRoleId, 3L))
                        .and(i -> i.ge(User::getAge, 20))
 
SELECT id,role_id,name,email,age FROM user WHERE ( (role_id = ? OR role_id = ?) ) AND ( (age >= ?) )
```

自定义查询则需要在 Mapper 接口中声明接口方法并实现在 xml 中:

```xml
MyPage<User> mySelectPage(@Param("pg") MyPage<User> myPage, @Param("ps") ParamSome paramSome);

<select id="mySelectPage" resultType="com.yangbingdong.service.mp.entity.User">
    select *
    from user
    where (age = #{pg.selectInt} and name = #{pg.selectStr})
       or (age = #{ps.yihao} and name = #{ps.erhao})
</select>
```

## 分页分页

配置:

```java
@Bean
public PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
    return paginationInterceptor;
}
```

编码:

```java
Page<User> page = new Page<>(1, 3);
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.lambda().ge(User::getAge, 1).orderByAsc(User::getAge);
IPage<User> result = mapper.selectPage(page, wrapper);
```

自定义 xml 需要分页, 只需要将 `IPage` 对象放在 Mapper 接口方法的第一个参数即可(必须是第一个):

```xml
List<User> iPageSelect(IPage<User> myPage);

<select id="iPageSelect" resultType="com.yangbingdong.service.mp.entity.User">
    select * from user
    <where>
        <if test="name!=null and name!=''">
            name like #{name}
        </if>
    </where>
</select>
```

每一次分页查询会自动 Count 操作, 并且模式自动优化, 通过下面参数空值:

```java
page.setOptimizeCountSql(false).setSearchCount(false);
```



## 自定义全局方法

1. 定义 SQL 方法:

   ```java
   public class SelectMaxId extends AbstractMethod {
   
       private static final String MAX_SQL = "SELECT IFNULL(MAX({}), 0) FROM {}";
       private static final String MAX_METHOD_NAME = "selectMaxId";
   
       @Override
       public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
           String sql = StrFormatter.format(MAX_SQL, tableInfo.getKeyColumn(), tableInfo.getTableName());
           SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
           return this.addSelectMappedStatementForOther(mapperClass, MAX_METHOD_NAME, sqlSource, Long.class);
       }
   }
   ```

   **注意:** `selectMaxId` 方法名要和下面第三点的方法名一致.

2. 注册

   ```java
   public class CustomLogicSqlInjector extends DefaultSqlInjector {
   
       @Override
       public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
           List<AbstractMethod> methodList = super.getMethodList(mapperClass);
           methodList.add(new SelectMaxId());
           return methodList;
       }
   }
   ```

3. 把方法定义到 BaseMapper:

   ```java
   public interface CustomBaseMapper<T> extends BaseMapper<T> {
       Long selectMaxId();
   }
   ```

4. 注册到 Spring 容器:

   ```java
   @Bean
   public CustomLogicSqlInjector myLogicSqlInjector() {
       return new CustomLogicSqlInjector();
   }
   ```

## 逻辑删除

配置:

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
```

实体类字段上加上`@TableLogic`注解:

```java
@TableLogic
private Boolean deleted;
```

## 自动填充

1. 实现元对象处理器接口：`MetaObjectHandler`

   ```java
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
   ```

2. 注解填充字段 `@TableField(.. fill = FieldFill.INSERT)`:

   ```java
   public class User {
   
       // 注意！这里需要标记为填充字段
       @TableField(.. fill = FieldFill.INSERT)
       private String fillField;
   
       ....
   }
   ```

3. 注册到 Spring 容器:

   ```java
   @Bean
   public CustomMetaObjectHandler customMetaObjectHandler() {
       return new CustomMetaObjectHandler();
   }
   ```


## 自定义拦截器

```java
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class PageSettingCacheInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if (args.length > 1 & args[1] instanceof Map) {
            HashMap<String, Object> paramMap = (HashMap<String, Object>) args[1];
            boolean matches = paramMap.entrySet().stream().anyMatch(entry -> entry.getValue() instanceof Page);
            if (matches) {
                Class<?> clazz = ms.getClass();
                Field useCache = clazz.getDeclaredField("useCache");
                useCache.setAccessible(true);
                useCache.set(ms, false);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        //do nothing
    }

}
```

然后注册到 Spring:

```java
@Bean
public PageSettingCacheInterceptor pageSettingCacheInterceptor() {
    return new PageSettingCacheInterceptor();
}
```

## 动态表名

```java
@Bean
public PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
    dynamicTableNameParser.setTableNameHandlerMap(new HashMap<String, ITableNameHandler>(2) {{
        put("user", (metaObject, sql, tableName) -> {
            // metaObject 可以获取传入参数，这里实现你自己的动态规则
            String year = "_2018";
            int random = new Random().nextInt(10);
            if (random % 2 == 1) {
                year = "_2019";
            }
            return tableName + year;
        });
    }});
    paginationInterceptor.setSqlParserList(Collections.singletonList(dynamicTableNameParser));
    return paginationInterceptor;
}
```

## 字段类型处理

可用于json字符串转换

```java
@Data
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class User {

    /**
     * 注意！！ 必须开启映射注解
     *
     * @TableName(autoResultMap = true)
     *
     * 以下两种类型处理器，二选一 也可以同时存在
     *
     * 注意！！选择对应的 JSON 处理器也必须存在对应依赖包
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Wallet wallet;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private OtherInfo otherInfo;

}
```

