package com.bwagih.scheduler.model.common.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

public class CustomJackson2JsonRedisSerializer extends Jackson2JsonRedisSerializer<Object> {

    private static final CustomJackson2JsonRedisSerializer instance = new CustomJackson2JsonRedisSerializer(Object.class);

    public CustomJackson2JsonRedisSerializer(Class<Object> type) {
        super(type);
        setObjectMapper(CustomObjectMapper.getInstance());
    }

    public CustomJackson2JsonRedisSerializer(JavaType javaType) {
        super(javaType);
    }

    @Override
    public void setObjectMapper(@NotNull ObjectMapper objectMapper) {
        super.setObjectMapper(objectMapper);
    }

    public static CustomJackson2JsonRedisSerializer getInstance() {
        return instance;
    }
}
