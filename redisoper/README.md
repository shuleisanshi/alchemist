# 使用说明

## 配置

```yml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    #连接超时时间
    timeout: 10s
    lettuce:
      pool:
        #最大连接数
        max-active: 13
        #最大阻塞等待时间(负数表示没限制)
        max-wait: 5s
        #最大空闲
        max-idle: 1
        #最小空闲
        min-idle: 0


alchemist:
  redisoper:
    serialize-type: protostuff  // 序列化方式
    orm-type: none  // 如果使用 MyBatis Plus 作为ORM 请填 mp
    dlock:  // 分布式锁配置
      enable: true  // 是否启用
      dLockType: redisson  // 使用 redisson 作为分布式锁, 可选还有 spring
    locator:  // 定位服务配置
      enable: true  // 是否启用
      type: tencent  // 服务商, 可需要的还有 gaode
      key: JVBBZ-DCPK3-6G33L-36QAM-YEQC5-WCBZQ
      sign: DHwOuC0wNVRBxIIQzfcubDBi67BxgeUp

#locator:
#  type: gaode
#  key: 96082be8ca6d5e3f6983bcf402c7117b
#  sign: 26a410713aa0f6f25be985eac5b52beb
```

## Redisoper

实现 `RedisoperAware<T>` 类, 注入 `GenericRedisoper<T>` 对象. 通过 `AbstractBeanPostProcessor` 实现的 Bean 注入.

实体类主键需要贴上 `@RedisPrimaryKey` 注解, 索引贴上 `@RedisIndex(name = "xxx", unique = false, order = 0)` 注解.

## CommonRedisoper

对外提供一些String类型的通用操作, 可直接注入.

## WrapperRedisoper

提供其他实体类或者集合类的缓存操作, 可直接注入.

## DLock

需要先通过配置开启.

在方法上贴 `@Lock(key = "'BuziKey:' + #args[0]", waitSecond = 2L, leaseSecond = 5L)` 注解.

`key` 支持 SpEL, `waitSecond` 为等待时间, 默认为2秒, `leaseSecond` 为释放时间, 默认为5秒.

## GEO

先配置定位服务商. 然后注入 `Locator` 对象, 调用 `locate` 方法即可定位.

注入 `RedisGeoHelper` 对象可操作 Redis 中位置相关操作. 

# TODO
-[ ] 元数据优化

-[ ] pipeline