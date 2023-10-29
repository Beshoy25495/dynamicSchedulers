package com.bwagih.scheduler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

import java.net.InetAddress;

@Configuration
@ComponentScan(basePackages = "com.knet.*")
@EnableTransactionManagement
@EnableRedisHttpSession
@EnableDiscoveryClient
@ConditionalOnConsulEnabled
@ConditionalOnMissingBean(type = "org.springframework.cloud.consul.discovery.ConsulLifecycle")
@AutoConfigureAfter(ConsulAutoServiceRegistrationAutoConfiguration.class)
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class, JooqAutoConfiguration.class})
@EnableScheduling
@EnableAspectJAutoProxy()
public class Bootstrap extends SpringBootServletInitializer
        implements WebApplicationInitializer, ApplicationContextAware {

    @Autowired(required = false)
    ConsulAutoServiceRegistration consulAutoServiceRegistration;

    public static void main(String[] args) {
        addCustomPlatformProperties();
        SpringApplication.run(Bootstrap.class, args);
    }

    public static void addCustomPlatformProperties() {
        try {
            System.setProperty("scheduler.host", InetAddress.getLocalHost().getCanonicalHostName());
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        Bootstrap.addCustomPlatformProperties();
        return application.sources(Bootstrap.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null != consulAutoServiceRegistration) {
            consulAutoServiceRegistration.start();
        }
    }

}

