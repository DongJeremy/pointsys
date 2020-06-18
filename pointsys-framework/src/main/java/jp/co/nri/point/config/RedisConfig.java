package jp.co.nri.point.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import jp.co.nri.point.dto.LoginUser;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {

    @Bean("redisTemplate")
    public RedisTemplate<String, LoginUser> redisTemplate(@Lazy RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, LoginUser> redis = new RedisTemplate<>();
        GenericToStringSerializer<String> keySerializer = new GenericToStringSerializer<String>(String.class);
        redis.setKeySerializer(keySerializer);
        redis.setHashKeySerializer(keySerializer);
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        redis.setValueSerializer(valueSerializer);
        redis.setHashValueSerializer(valueSerializer);
        redis.setConnectionFactory(connectionFactory);

        return redis;
    }

}
