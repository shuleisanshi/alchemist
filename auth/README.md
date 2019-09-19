# 特性

集成 `auth` 模块后, 默认所有的请求需要鉴权, 即请求 Header 中需要带上 `Authorization`.

鉴权通过拦截器实现, 请看 `AuthorizationInterceptor`.

如果 Controller 方法上贴有 `@IgnoreAuth` 注解, 则跳过鉴权, 否则进入 `AbstractJwtAuthorizationPreHandler# preHandleAuth` 方法进行鉴权.

`preHandleAuth` 中先对 `Authorization` 中的基本信息进行校验(Token 完整性, 合法性以及过期校验), 然后进入 `preHandle` 方法, 这个是需要自己继承 `AbstractJwtAuthorizationPreHandler` 并实现的自己的业务校验. 最后, 如果 Token 快过期将会重新颁发一次 Token.

`Jwt` 本身并没有状态, `auth` 模块利用了 Redis 与 Caffeine 做了状态绑定, Caffeine 作为一级缓存不需要每次都查询 Redis.

# 使用

配置文件:

```yml
alchemist:
  auth:
    enable-jwt-session: true  // 是否启用Session, 因为 Jwt 本身无状态
    session-expire-second: 86400 // jwt session 过期时间
    local-session-cache-maximum-size: 50000  //  Session本地缓存最大存储量
    local-session-expire-second: 300  // Session本地缓存过期时间
    sign-key: HelloWorld  // Token 签名的Key
```

继承 `JwtPayload`, 定义自己的业务字段:

```java
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MyJwtPayload extends JwtPayload<MyJwtPayload> {

    private String name;
}
```

继承 `AbstractJwtAuthorizationPreHandler`, 实现自己的业务鉴权:

```java
@Slf4j
@Component
public class MyAuthorizationPreHandler extends AbstractJwtAuthorizationPreHandler<MyJwtPayload> {

	@Override
	protected void preHandle(AuthContext<MyJwtPayload> authContext) {
		log.info("Jwt: {}", authContext.getJwt());
	}
}
```

颁发 Token: `JwtOperator#grantAuthJwt`

清除 Token Session: `JwtOperator#eraseSession`

# TODO

RBAC 模块