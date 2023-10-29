package com.bwagih.scheduler.listner;

import com.bwagih.scheduler.utils.Defines;
import com.bwagih.scheduler.model.SchedulerJobModel;
import com.bwagih.scheduler.service.SchedulerConfigService;
import com.bwagih.scheduler.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class LoadSchedulerConfig {

    private final SchedulerConfigService schedulerConfigService;
    private final RedisService<SchedulerJobModel> redisService;

    @EventListener
    public void loadCashKeysLocalOnStartUp(ApplicationStartedEvent event) {
        redisService.deleteKeysBasedOnPrefix(Defines.BASE_PACKAGE_PREFIX);
        schedulerConfigService.loadSchedulerJobsConfig(Defines.BASE_PACKAGE_PREFIX);
    }

}
