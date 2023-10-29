package com.bwagih.scheduler.model;

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
