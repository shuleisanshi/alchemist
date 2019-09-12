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
- [x] 请求体多读, 通过 `WebUtils.getNativeRequest(currentRequest(), RequestBodyCachingWrapper.class)` 获取 `RequestBodyCachingWrapper` 对象, .

## 使用说明

配置:

```yml
alchemist:
  mvc:
    oplog: true  // 是否启用日志模块
    print-request-param-if-error: true // 请求发生异常时, 是否打印请求信息, 包括 param, body, header 等
    print-request-info-if-business-error: true  // 发生 BusinessException 时是否打印请求信息
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
3. 实现 `OpLogEventSourceHandler` 接口并交给 Spring 管理, 处理自己的日志, 可以只打印, 也可以选择持久化到数据库.

> 日志事件的发送依赖于 Disruptor 模块.

## 其他异常打印请求信息

请看 `GlobalExceptionHandler` 类.

需要配置:

```yml
alchemist:
  mvc:
    print-request-info-if-error: true // 默认为false
    print-request-info-if-business-error: true 
```

当以上两个配置任意一个为 true, 将会配置 `RequestBodyCachingFilter`, 除了 `GET` 以及 `OPTIONS` 意外的请求都会将 `HttpServletRequest` 包装成 `RequestBodyCachingWrapper` 进行 `requestBody` 的缓存.

## Spring MVC Restful 优化

具体实现在 `EnhanceRequestMappingHandlerMapping`.

对于 Restful 风格的请求,  `@RequestMapping` 必须设置 `name` 属性, 并且请求 `Header` 必须设置 `X-Inner-Action` 参数, 值与 `name` 一致.

通过此手段可以达到与直接匹配一样的速度.

## 其他

* 配置了 `IpInterceptor`, 获取请求ip并放到 `MDC` 以及 request attribute 中, key 都为 `IP`.

* 配置了 `MDCCleanFilter`, 用于清除 `MDC`.
* 扩展了 Log4j2 (请看 `SpringEnvironmentLookup`), 可以在 `log4j2.xml` 中获取到配置文件中的信息.
* 自定义 `SpringApplicationRunListener` (请看 `MvcProfileIncludeInitializer`), 在环境变量中添加了 `mvc` 模块的配置文件.