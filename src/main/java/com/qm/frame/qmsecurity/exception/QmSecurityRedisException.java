package com.qm.frame.qmsecurity.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 10:50
 * @Description QmSecurityRedis异常类
 */
public class QmSecurityRedisException extends RuntimeException {

    private static String msg = "QmSecurityRedis连接发生了异常！";

    public QmSecurityRedisException(Throwable cause) {
        super(msg,cause);
    }

}
