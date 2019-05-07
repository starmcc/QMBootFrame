package com.qm.frame.qmsecurity.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:50
 * @Description QmSecurity安全框架Redis默认模板
 */
@Component
public class QmSecurityRedisClientTemplate implements QmSecurityRedisClient{

    private static QmSecurityRedisClientTemplate qmSecurityRedisClientTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String k, Object v) {
        try {
            qmSecurityRedisClientTemplate.redisTemplate.opsForValue().set(k, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(String k, Object v, long exp) {
        try {
            qmSecurityRedisClientTemplate.redisTemplate.opsForValue().set(k, v, exp, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(String k) {
        return k == null ? null : qmSecurityRedisClientTemplate.redisTemplate.opsForValue().get(k);
    }

    //@PostConstruct修饰的方法会在服务器加载Servle的时候运行，
    // 并且只会被服务器执行一次。
    // PostConstruct在构造函数之后执行,init()方法之前执行
    @PostConstruct
    public void init() {
        qmSecurityRedisClientTemplate = this;
        qmSecurityRedisClientTemplate.redisTemplate = this.redisTemplate;
    }
}
