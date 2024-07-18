package com.ecommerce.userservice.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheStore<K, V> {
    private final Map<K, V> cache = new ConcurrentHashMap<>();
    
    public void put(K key, V value) {
        cache.put(key, value);
    }
    
    public V get(K key) {
        return cache.get(key);
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
