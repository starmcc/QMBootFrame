package com.qm.frame.qmsecurity.cache;

import java.util.TimerTask;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/11 23:53
 * @Description map缓存定时器
 */
public class MapCacheTimer extends TimerTask {

    private QmSecurityCache qmSecurityCache;
    private String key;

    public MapCacheTimer(String key, QmSecurityCache qmSecurityCache) {
        this.key = key;
        this.qmSecurityCache = qmSecurityCache;
    }


    @Override
    public void run() {
        qmSecurityCache.remove(key);
    }
}
