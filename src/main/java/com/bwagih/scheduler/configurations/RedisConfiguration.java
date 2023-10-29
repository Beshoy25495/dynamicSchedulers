package com.bwagih.scheduler.configurations;


import com.bwagih.scheduler.model.common.config.RedisConnection;
import com.bwagih.scheduler.model.common.jackson.CustomJackson2JsonRedisSerializer;
import io.lettuce.core.ClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

    @Autowired
    RedisConnection redisConnection;

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate<String, Object> template = null;
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = CustomJackson2JsonRedisSerializer.getInstance();
        try {
            template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory());

            template.setValueSerializer(jackson2JsonRedisSerializer);
            template.setHashKeySerializer(jackson2JsonRedisSerializer);

            template.setHashValueSerializer(jackson2JsonRedisSerializer);
            template.setValueSerializer(jackson2JsonRedisSerializer);

        } catch (Exception e) {
            logger.error("Error getting Redis Template connection ", e);
        }
        return template;
    }


    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory connectionFactory = null;
        try {
            ClientOptions clientOptions = ClientOptions.builder()
                    .cancelCommandsOnReconnectFailure(true)
                    .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                    .build();

            RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(redisConnection.getHost(),
                    redisConnection.getPort());
            LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder().clientOptions(clientOptions).build();
            connectionFactory = new LettuceConnectionFactory(standaloneConfig, lettuceClientConfiguration);
        } catch (Exception e) {
            logger.error("Error in LettuceConnectionFactory ::> {} ", e.getMessage(), e);
        }
        return connectionFactory;
    }

}
