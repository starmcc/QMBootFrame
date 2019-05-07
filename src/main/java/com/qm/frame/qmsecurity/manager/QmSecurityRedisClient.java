package com.qm.frame.qmsecurity.manager;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:47
 * @Description QmSecurity安全框架Redis接口
 */
public interface QmSecurityRedisClient {

    void set(String k,Object v);

    void set(String k,Object v,long exp);

    Object get(String k);

}
