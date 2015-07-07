package com.ogzcm.template;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public abstract class AbstractRedisTemplate {

	@Resource(name="redisTemplate")
	private RedisTemplate<String, Object> template;
	
	public RedisTemplate<String, Object> getRedisTemplate() {
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
		template.setValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );

		return template;
	}
	
	public abstract void add(Object object) throws ClassCastException;
	public abstract void update(Object object) throws ClassCastException;
	public abstract void delete(long id);
}
