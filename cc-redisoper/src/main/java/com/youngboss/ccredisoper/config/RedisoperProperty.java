package com.youngboss.ccredisoper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ybd
 * @date 19-3-27
 * @contact yangbingdong1994@gmail.com
 */
@Data
@ConfigurationProperties(RedisoperProperty.PREFIX)
public class RedisoperProperty {

	public static final String PREFIX = "youngboss.redisoper";

	private SerializeType serializeType = SerializeType.PROTOSTUFF;

	private OrmType ormType = OrmType.MP;
}
