package com.qm.frame.qmsecurity.manager;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:47
 * @Description QmSecurity安全框架Redis接口
 */
public interface QmSecurityRedisClient {

    /**
     * 设置值到缓存中
     * @param k 键
     * @param v 值
     */
    void set(String k,Object v);

    /**
     * 设置值到缓存中
     * @param k 键
     * @param v 值
     * @param exp 单位(秒)
     */
    void set(String k,Object v,long exp);

    /**
     * 获取缓存中该键的值
     * @param k 键
     * @return 值
     */
    Object get(String k);

}
