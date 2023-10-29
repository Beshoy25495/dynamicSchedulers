package com.bwagih.scheduler.service;


import com.bwagih.scheduler.model.SchedulerJobModel;
import com.bwagih.scheduler.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerConfigService {

    private final SchedulerConfigRepository configRepository;
    private final RedisService redisService;

    public void loadSchedulerJobsConfig(String prefix) {
        List<SchedulerJobModel> jobsConfig;
        Map<String, SchedulerJobModel> parametersMap;
        try {
            jobsConfig = configRepository.getSchedulerJobsConfig();
            if (Objects.nonNull(jobsConfig)) {
                parametersMap = ConvertJobsConfigToMap(jobsConfig, prefix);
                redisService.addBunchOfData(parametersMap);
            }
        } catch (Exception e) {
            log.error("ERROR THREW communicate with db {}..", e.getStackTrace(), e);
        }

    }

    private Map<String, SchedulerJobModel> ConvertJobsConfigToMap(List<SchedulerJobModel> jobsConfig, String prefix) {
        Map<String, SchedulerJobModel> parametersMap = new HashMap<>();
        jobsConfig.parallelStream().forEach(jobConfig -> {
            jobConfig.setJobName(prefix.concat(jobConfig.getJobName()));
            parametersMap.put(prefix.concat(jobConfig.getJobName()), jobConfig);
        });
        return parametersMap;
    }

}
