package com.hongqisi.service;

public interface CacheService {

    /**
     * 根据key的前缀来删除匹配该模式下的所有key-value
     * @param keyPrefix
     */
    public void removeFromCache(String keyPrefix);

}
