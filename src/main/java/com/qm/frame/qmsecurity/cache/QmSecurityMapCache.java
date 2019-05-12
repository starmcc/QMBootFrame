package com.qm.frame.qmsecurity.cache;

import java.util.Map;
import java.util.Timer;

/**
 * map缓存容器
 * 注释时间：2019/5/12 6:39
 */
public class QmSecurityMapCache implements QmSecurityCache {
    /**
     * 缓存
     */
    private final Map<String, Object> map;

    public QmSecurityMapCache(Map<String, Object> backingMap) {
        if (backingMap == null) {
            throw new IllegalArgumentException("Backing map cannot be null.");
        } else {
            this.map = backingMap;
        }
    }

    @Override
    public Object get(String key) {
        return this.map.get(key);
    }

    @Override
    public void put(String key, Object value) {
        this.map.put(key, value);
    }

    @Override
    public void put(String key, Object value, long exp) {
        this.map.put(key, value);
        //延迟执行删除!
        new Timer().schedule(new MapCacheTimer(key, this), exp * 1000);
    }

    @Override
    public boolean remove(String key) {
        return this.map.remove(key) != null;
    }
}