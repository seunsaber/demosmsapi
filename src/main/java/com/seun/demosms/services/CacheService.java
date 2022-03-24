package com.seun.demosms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;

    @Autowired
    public CacheService (RedisTemplate redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }
    public void addToCache(String cacheName, String key, String value){
        hashOperations.put(cacheName, key, value);
    }

    public String getItem(String cacheName, String key){
        return (String) hashOperations.get(cacheName, key);
    }

    public boolean hasKay(String cacheName ,String key){
        return hashOperations.hasKey(cacheName, key);
    }
}
