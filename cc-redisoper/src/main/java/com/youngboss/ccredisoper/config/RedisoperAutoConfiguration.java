package com.youngboss.ccredisoper.config;

import com.youngboss.ccredisoper.core.beanprocessor.RedisoperBeanPostProcessor;
import com.youngboss.ccredisoper.core.beanprocessor.RedisoperMpBeanPostProcessor;
import com.youngboss.ccredisoper.extend.commom.CommonRedisoper;
import com.youngboss.ccredisoper.extend.wrapper.WrapperRedisoper;
import com.youngboss.ccredisoper.serilize.FSTSerializer;
import com.youngboss.ccredisoper.serilize.KryoSerializer;
import com.youngboss.ccredisoper.serilize.ProtostuffSerializer;
import com.youngboss.ccredisoper.serilize.Serializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;

/**
 * @author ybd
 * @date 19-03-20
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@EnableConfigurationProperties(RedisoperProperty.class)
@Import(CustomLettuceConnectionConfiguration.class)
public class RedisoperAutoConfiguration {

	public static final String CcRedisoperBeanPostProcessorBeanName = "ccRedisoperBeanPostProcessor";

	@Resource
	private RedisoperProperty redisoperProperty;

	@Bean("serializer")
	public Serializer serializer() {
		switch (redisoperProperty.getSerializeType()) {
			case PROTOSTUFF:
				return new ProtostuffSerializer();
			case FST:
				return new FSTSerializer();
			case KRYO:
				return new KryoSerializer();
			default:
				return new ProtostuffSerializer();
		}
	}

	@Primary
	@Bean("redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		return buildRedisTemplate(redisConnectionFactory);
	}

	@Bean("tranRedisTemplate")
	public RedisTemplate<String, Object> tranRedisTemplate(@Qualifier("tranRedisConnectionFactory") RedisConnectionFactory tranRedisConnectionFactory) {
		RedisTemplate<String, Object> template = buildRedisTemplate(tranRedisConnectionFactory);
		template.setEnableTransactionSupport(true);
		return template;
	}

	@ConditionalOnMissingBean(name = CcRedisoperBeanPostProcessorBeanName)
	@Bean(CcRedisoperBeanPostProcessorBeanName)
	public BeanPostProcessor ccRedisoperBeanPostProcessor() {
		if (OrmType.MP.equals(redisoperProperty.getOrmType())) {
			return new RedisoperMpBeanPostProcessor();
		}
		return new RedisoperBeanPostProcessor();
	}

	@Bean
	public WrapperRedisoper ccWrapperRedisoper() {
		return new WrapperRedisoper();
	}

	@Bean
	public CommonRedisoper commonRedisoper() {
		return new CommonRedisoper();
	}

	private RedisTemplate<String, Object> buildRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());
		template.setExposeConnection(true);
		return template;
	}

}
