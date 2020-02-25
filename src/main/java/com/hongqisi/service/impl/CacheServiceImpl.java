package com.hongqisi.service.impl;

import com.hongqisi.cache.JedisUtil;
import com.hongqisi.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class CacheServiceImpl implements CacheService {

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keys=jedisKeys.keys(keyPrefix+"*");
        for(String key:keys){
            jedisKeys.del(key);
        }
    }
}
