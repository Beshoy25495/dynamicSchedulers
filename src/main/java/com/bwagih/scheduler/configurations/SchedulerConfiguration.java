package com.bwagih.scheduler.configurations;


import com.bwagih.scheduler.model.common.config.TaskSchedulerConnectionPool;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfiguration implements SchedulingConfigurer {


    @Autowired
    TaskSchedulerConnectionPool schedulerConnectionPool;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean
    @Async
    @SchedulerLock(name = "lockedTask", lockAtMostFor = "PT5M", lockAtLeastFor = "PT2M")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(Math.toIntExact(schedulerConnectionPool.getPoolSize()));
        scheduler.setAwaitTerminationSeconds(Math.toIntExact(schedulerConnectionPool.getAwaitTerminationSeconds()));
        scheduler.setDaemon(schedulerConnectionPool.getIsDaemon());
        scheduler.setThreadNamePrefix(schedulerConnectionPool.getThreadNamePrefix());
        scheduler.initialize();
        return scheduler;
    }

}
