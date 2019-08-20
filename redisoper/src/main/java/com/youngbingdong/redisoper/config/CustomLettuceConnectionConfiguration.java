package com.youngbingdong.redisoper.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.resource.ClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

/**
 * @author ybd
 * @date 19-4-28
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@ConditionalOnClass(RedisClient.class)
@EnableConfigurationProperties(RedisProperties.class)
public class CustomLettuceConnectionConfiguration {

	@Autowired
	private RedisProperties properties;

	@Bean
	public GenericObjectPoolConfig<?> genericObjectPoolConfig() {
		RedisProperties.Pool pool = properties.getLettuce().getPool();
		GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(pool.getMaxActive());
		config.setMaxIdle(pool.getMaxIdle());
		config.setMinIdle(pool.getMinIdle());
		if (pool.getMaxWait() != null) {
			config.setMaxWaitMillis(pool.getMaxWait().toMillis());
		}
		return config;
	}

	@Primary
	@Bean(name = "redisConnectionFactory")
	public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) {
		LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(clientResources);
		return createLettuceConnectionFactory(clientConfig);
	}

	@Bean(name = "tranRedisConnectionFactory")
	public LettuceConnectionFactory tranRedisConnectionFactory(ClientResources clientResources) {
		LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(clientResources);
		return createLettuceConnectionFactory(clientConfig);
	}

	private LettuceConnectionFactory createLettuceConnectionFactory(LettuceClientConfiguration clientConfiguration) {
		return new LettuceConnectionFactory(getStandaloneConfig(), clientConfiguration);
	}

	private RedisStandaloneConfiguration getStandaloneConfig() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(this.properties.getHost());
		config.setPort(this.properties.getPort());
		config.setPassword(RedisPassword.of(this.properties.getPassword()));
		config.setDatabase(this.properties.getDatabase());
		return config;
	}

	private LettuceClientConfiguration getLettuceClientConfiguration(ClientResources clientResources) {
		LettuceClientConfigurationBuilder builder = createBuilder();
		applyProperties(builder);
		builder.clientResources(clientResources);
		return builder.build();
	}

	private LettuceClientConfigurationBuilder createBuilder() {
		return LettucePoolingClientConfiguration.builder()
												.poolConfig(genericObjectPoolConfig());
	}

	private void applyProperties(LettuceClientConfigurationBuilder builder) {
		if (properties.isSsl()) {
			builder.useSsl();
		}
		if (properties.getTimeout() != null) {
			builder.commandTimeout(properties.getTimeout());
		}
		if (properties.getLettuce() != null) {
			RedisProperties.Lettuce lettuce = properties.getLettuce();
			if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
				builder.shutdownTimeout(properties.getLettuce().getShutdownTimeout());
			}
		}
	}
}
