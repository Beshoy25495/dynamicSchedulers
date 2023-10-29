package com.bwagih.scheduler.model.common.config;


import lombok.Data;

@Data
public class RedisConnection {

    private String host = "localhost";

    private Integer port = 6379;

}
