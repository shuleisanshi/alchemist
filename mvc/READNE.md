# MVC 通用配置

## 特性

- [x] 集成 *FastJson* 返回JSON格式报文
- [x] `@Rest` 注解返回统一响应报文, 详情见 `GlobalControllerAdvisor`
- [x] 配置了 `IpInterceptor`, 可通过 Request 获取当前访问用户的IP, 也可以在 `Log4j2.xml` 通过 `%X{IP}` 获取.
- [x] 使用 `Log4j2` 作为日志框架, 并启用 `Disruptor` 异步输入日志.
- [x] 统一异常处理
- [x] 集成 `Validation` 并统一处理验证异常
- [x] 集成操作日志记录模块, 并使用 `Disruptor` 异步处理, 通过 `@OpLog` 驱动.
- [x] 使用 Undertow 作为 Web 容器, 配置了优雅关机.

## 使用说明

配置:

```yml
alchemist:
  mvc:
    oplog: true  // 是否启用日志模块
    disruptor:
      power: 11
      worker: 2
```

## @Rest 注解

Controller 贴上 `@Rest("/path")` 注解, 方法声明为 `void` 或直接返回实体, `GlobalControllerAdvisor` 将封装成 `Response<T>` 对象并返回JSON格式的响应.

若要返回自定义的 JSON 格式, 不使用 `@Rest` 注解即可.



## @OpLog 操作日志

`@OpLog("操作名称")` 可用于记录操作日志, 但必须先通过参数开启.

操作步骤:

1. 在 `Conreoller` 的方法贴上 `@OpLog("操作名称")`.
2. 实现 `OpLogContextBuilder<T extends OpLogContext>` 类并交给 Spring 管理, 泛型中的 `T` 需要继承 `OpLogContext`, 可添加自定义字段.
3. 实现 `OpLogEventSourceHandler` 接口并交给 Spring 管理, 处理自己的日志.

# TODO

RBAC 通用模块.