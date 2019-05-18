package com.qm.frame.qmsecurity.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/12 5:30
 * @Description 缓存异常
 */
public class QmSecurityCacheException extends RuntimeException {

    private static final String MSG = "Cache发生了一个异常！";

    public QmSecurityCacheException() {
        super(MSG);
    }

    public QmSecurityCacheException(Throwable cause) {
        super(MSG, cause);
    }
}
