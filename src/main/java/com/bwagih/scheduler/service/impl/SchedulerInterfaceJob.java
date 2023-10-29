package com.bwagih.scheduler.service.impl;

import com.bwagih.scheduler.enums.JobTypes;
import com.bwagih.scheduler.commons.TnTaskExecute;
import com.bwagih.scheduler.model.SchedulerJobModel;
import org.springframework.scheduling.Trigger;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public interface SchedulerInterfaceJob {
    static boolean isaDuration(SchedulerJobModel clazz) {
        return JobTypes.DURATION.name().equals(clazz.getJobType()) && (Objects.nonNull(clazz.getStartTo()));
    }

    static Trigger durationTrigger(SchedulerJobModel clazz) {
        return triggerContext -> Objects.nonNull(clazz.getStartFrom())
                ? new Date(Duration.between(clazz.getStartFrom().toInstant(),
                clazz.getStartTo().toInstant()).getNano())
                : clazz.getStartTo();
    }

    static Trigger delayTrigger(SchedulerJobModel clazz) {
        return triggerContext -> {
            Instant lastCompletionTime = Objects.nonNull(triggerContext.lastCompletionTime())
                    ? Objects.requireNonNull(triggerContext.lastCompletionTime()).toInstant() : Instant.now();
            return Date.from(lastCompletionTime.plusMillis(Integer.parseInt(String.valueOf(clazz.getDelay())) * 1000L));
        };
    }

    static boolean isaCron(SchedulerJobModel clazz) {
        return JobTypes.CRON.name().equals(clazz.getJobType()) && Objects.nonNull(clazz.getCron());
    }

    static boolean isaDelay(SchedulerJobModel clazz) {
        return JobTypes.DELAY.name().equals(clazz.getJobType()) && isaValidData(clazz.getDelay());
    }

    static boolean isaValidData(BigInteger data) {
        return data.compareTo(BigInteger.ZERO) > 0;
    }

    void execute();

    Runnable getTask(SchedulerJobModel clazz);

    TnTaskExecute<SchedulerJobModel> getaBeenClass(SchedulerJobModel clazz) throws ClassNotFoundException;
}
