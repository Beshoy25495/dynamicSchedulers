package com.bwagih.scheduler.configurations;


import com.bwagih.scheduler.utils.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@Slf4j
public class ServiceConfiguration {

    @Autowired
    HttpService httpService;

    @Bean
    @LoadBalanced
    public WebClient webClient() {

        return WebClient.builder()
                .filters(httpService.getFilters())
                .clientConnector(new ReactorClientHttpConnector(httpService.getHttpClient()))
                .build();
    }




}
