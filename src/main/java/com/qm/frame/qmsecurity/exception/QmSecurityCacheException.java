package com.qm.frame.qmsecurity.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/12 5:30
 * @Description 缓存异常
 */
public class QmSecurityCacheException extends Exception {
    public QmSecurityCacheException() {
        super();
    }

    public QmSecurityCacheException(String message) {
        super(message);
    }

    public QmSecurityCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public QmSecurityCacheException(Throwable cause) {
        super(cause);
    }
}
