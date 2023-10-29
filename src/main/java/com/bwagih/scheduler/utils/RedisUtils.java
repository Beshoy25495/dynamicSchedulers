package com.bwagih.scheduler.utils;

import com.bwagih.scheduler.model.SchedulerJobModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisUtils<T extends SchedulerJobModel> {

    private final RedisTemplate redisTemplate;

    public void addBunchOfData(Map<String,T> parametersMap) {
        redisTemplate.opsForValue().multiSet(parametersMap);
    }

    public List<T> getBunchOfData(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public List<T> getBunchOfDataBasedOnPrefix(String prefix) {
        Set<String> keys = getKeysBasedOnPrefix(prefix);
        return getBunchOfData(keys.stream().collect(Collectors.toList()));
    }

    public void addOne(String key, T value) {
        if (isKeyAlreadyExist(key))
            log.debug("update Key [ {} ] , with new value [ {} ] ..", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    public T getOne(String key) {
        if (isKeyAlreadyExist(key))
            return (T) redisTemplate.opsForValue().get(key);
        return null;
    }


    public Boolean deleteOne(String key) {
        if (isKeyAlreadyExist(key))
            return redisTemplate.opsForValue().getOperations().delete(key);
        return null;
    }

    public void deleteBunchOfKeys(List<String> keys) {
        keys.forEach(this::deleteOne);
    }

    public void deleteKeysBasedOnPrefix(String prefix) {
        Set<String> keys = getKeysBasedOnPrefix(prefix);
        if (Objects.nonNull(keys))
            deleteBunchOfKeys(keys.stream().collect(Collectors.toList()));
    }

    private Set<String> getKeysBasedOnPrefix(String prefix) {
        return (Set<String>) redisTemplate.keys(prefix);
    }

    public Boolean isKeyAlreadyExist(String key) {
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().getOperations().hasKey(key))) {
            return Boolean.TRUE;
        }
        log.debug("KEY [ {} ] is not exist..", key);
        return Boolean.FALSE;
    }


}
