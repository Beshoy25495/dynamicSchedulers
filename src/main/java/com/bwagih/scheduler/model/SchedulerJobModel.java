package com.bwagih.scheduler.model;

import com.knet.tradenet.tnscheduler.db.tables.pojos.TnSchedulerJob;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchedulerJobModel extends TnSchedulerJob {

    private Map<String, Object> data = new HashMap<>();

}
