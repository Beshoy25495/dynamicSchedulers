package com.bwagih.scheduler.service;


import com.bwagih.scheduler.commons.TnTaskExecute;
import com.bwagih.scheduler.model.SchedulerJobModel;
import com.bwagih.scheduler.service.impl.SchedulerInterfaceJob;
import com.bwagih.scheduler.utils.Defines;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public abstract class SchedulerJobService implements SchedulerInterfaceJob {

    private final TaskScheduler taskScheduler;

    private final ApplicationContext applicationContext;

    private final RedisService<SchedulerJobModel> redisService;
    private static final List<SchedulerJobModel> jobsConfig = new ArrayList<>();


    @PostConstruct
    private void init() {
        jobsConfig.addAll(redisService.getBunchOfDataBasedOnPrefix(Defines.BASE_PACKAGE_PREFIX));
        execute();
    }


    @Override
    public void execute() {
        jobsConfig.parallelStream().forEach(clazz -> {
            taskScheduler.schedule(getTask(clazz), getTrigger(clazz));
            redisService.addOne(clazz.getJobName() + "-" + System.currentTimeMillis(), clazz);
        });
    }

    @Override
    public Runnable getTask(SchedulerJobModel clazz) {
        return () -> {
            TnTaskExecute<SchedulerJobModel> systemClass;
            try {
                systemClass = getaBeenClass(clazz);
                systemClass.execute(clazz);
            } catch (ClassNotFoundException e) {
                log.error("ClassNotFound ::> {}  - ERROR MESSAGE ::> {} ..", clazz, e.getStackTrace(), e);
                //TODO @Async increment number of failure if greater than Max number of
                // Failure then delete that task from cash , and warning msg (class not found) {Listener}
            }
        };
    }


    @Override
    public TnTaskExecute<SchedulerJobModel> getaBeenClass(SchedulerJobModel clazz) throws ClassNotFoundException {
        return (TnTaskExecute<SchedulerJobModel>) applicationContext.getBean(Class.forName(clazz.getJobName()));
    }


    private static Trigger getTrigger(SchedulerJobModel clazz) {
        if (SchedulerInterfaceJob.isaCron(clazz)) {
            return new CronTrigger(clazz.getCron(), TimeZone.getTimeZone(TimeZone.getDefault().toZoneId()));
        } else if (SchedulerInterfaceJob.isaDelay(clazz)) {
            return SchedulerInterfaceJob.delayTrigger(clazz);
        } else if (SchedulerInterfaceJob.isaDuration(clazz)) {
            return SchedulerInterfaceJob.durationTrigger(clazz);
        } else {
            SchedulerJobService.log.error("triggerContext ::> {}  - ERROR MESSAGE ::> {}", clazz, "please check jobType" +
                    "    {CRON,\n" + "    DELAY,\n" + "    DURATION}");
            return triggerContext -> null;
        }

    }

}
