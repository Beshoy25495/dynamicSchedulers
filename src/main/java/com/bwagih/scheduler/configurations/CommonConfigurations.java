package com.bwagih.scheduler.configurations;

import com.bwagih.scheduler.model.common.config.RedisConnection;
import com.bwagih.scheduler.model.common.config.TaskSchedulerConnectionPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfigurations {


    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisConnection redisConnection() {
        return new RedisConnection();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.task.scheduling")
    public TaskSchedulerConnectionPool grpcConnectionPool() {
        return new TaskSchedulerConnectionPool();
    }

}