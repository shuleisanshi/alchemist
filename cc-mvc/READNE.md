# MVC 通用配置

- [x] 集成 *FastJson* 返回JSON格式报文
- [x] `@Rest` 注解返回统一响应报文, 详情见 `GlobalControllerAdvisor`
- [x] 配置了 `IpInterceptor`, 可通过 Request 获取当前访问用户的IP, 也可以在 `Log4j2.xml` 通过 `%X{IP}` 获取.
- [x] 使用 `Log4j2` 作为日志框架, 并启用 `Disruptor` 异步输入日志.
- [x] 统一异常处理
- [x] 集成 `Validation` 并统一处理验证异常
- [x] 集成操作日志记录模块, 并使用 `Disruptor` 异步处理, 通过 `@OpLog` 驱动.
- [x] 使用 Undertow 作为 Web 容器, 配置了优雅关机.
