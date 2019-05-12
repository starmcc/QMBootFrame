package com.qm.frame.qmsecurity.cache;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:47
 * @Description QmSecurity安全框架缓存接口
 */
public interface QmSecurityCache {

    /**
     * 设置值到缓存中
     *
     * @param key   键
     * @param value 值
     */
    void put(String key, Object value);

    /**
     * 设置值到缓存中
     *
     * @param key   键
     * @param value 值
     * @param exp   单位(秒)
     */
    void put(String key, Object value, long exp);

    /**
     * 获取缓存中该键的值
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 删除缓存中的key对应的k,v
     *
     * @param key
     * @return
     */
    boolean remove(String key);
}
