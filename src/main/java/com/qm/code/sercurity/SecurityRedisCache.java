package com.qm.code.sercurity;

import com.qm.frame.basic.util.QmRedisClient;
import com.qm.frame.qmsecurity.cache.QmSecurityCache;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:50
 * @Description QmSecurity安全框架缓存默认模板
 */
public class SecurityRedisCache implements QmSecurityCache {

    @Override
    public void put(String k, Object v) {
        QmRedisClient.set(k, v);
    }

    @Override
    public void put(String k, Object v, long exp) {
        QmRedisClient.set(k, v, exp);
    }

    @Override
    public Object get(String k) {
        return QmRedisClient.get(k);
    }

    @Override
    public boolean remove(String key) {
        return QmRedisClient.del(key);
    }
}
