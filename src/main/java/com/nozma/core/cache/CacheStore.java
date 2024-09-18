package com.nozma.core.cache;

import com.nozma.core.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CacheStore<K, V> {
    private final Map<K, V> cache = new ConcurrentHashMap<>();
    
    public void add(K key, V value) {
        if (CommonUtil.isNonNullOrNonEmpty(key) && CommonUtil.isNonNullOrNonEmpty(value)) {
            cache.put(key, value);
            log.info("Key {} is added", key);
        }
    }
    
    public V get(K key) {
        return cache.getOrDefault(key, null);
    }
    
    public V getOrDefault(K key, V defaultValue) {
        return cache.getOrDefault(key, defaultValue);
    }
    
    public void remove(K key) {
        cache.remove(key);
    }
    
    public void putAndIncrease(K key, V defaultValue) {
        cache.put(key, getOrDefault(key, defaultValue));
    }
}
