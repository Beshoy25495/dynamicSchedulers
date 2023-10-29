package com.bwagih.scheduler.repository;


import com.bwagih.scheduler.model.SchedulerJobModel;
import com.knet.tradenet.tnscheduler.db.Tables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfigRepository {

    private final DSLContext dslContext;

    public List<SchedulerJobModel> getSchedulerJobsConfig() {
        return dslContext.selectFrom(Tables.TN_SCHEDULER_JOB).fetchInto(SchedulerJobModel.class);
    }

}
