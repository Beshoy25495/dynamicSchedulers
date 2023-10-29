package com.bwagih.scheduler.service;

import com.bwagih.scheduler.model.SchedulerJobModel;
import com.bwagih.scheduler.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService<T extends SchedulerJobModel> {

    private final RedisUtils<T> redisUtils;

    public void addBunchOfData(Map<String, T> parametersMap) {
        redisUtils.addBunchOfData(parametersMap);
    }

    public void deleteKeysBasedOnPrefix(String prefix) {
        redisUtils.deleteKeysBasedOnPrefix(prefix);
    }

    public List<T> getBunchOfDataBasedOnPrefix(String prefix) {
        return redisUtils.getBunchOfDataBasedOnPrefix(prefix);
    }

    public void addOne(String kay ,T value) {
        redisUtils.addOne(kay , value);
    }

}
