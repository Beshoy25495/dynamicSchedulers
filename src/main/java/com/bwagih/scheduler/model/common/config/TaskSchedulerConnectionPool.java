package com.bwagih.scheduler.model.common.config;

import lombok.Data;

@Data
public class TaskSchedulerConnectionPool {
    private Long poolSize = 20L;
    private Long awaitTerminationSeconds = 600L;
    private Boolean isDaemon = Boolean.FALSE;
    private String threadNamePrefix = "TNSCHEDULER-";

}
