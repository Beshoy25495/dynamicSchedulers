package com.bwagih.scheduler.commons;

import com.bwagih.scheduler.model.SchedulerJobModel;

public interface TnTaskExecute<T extends SchedulerJobModel> {
    public void execute(T job);
}