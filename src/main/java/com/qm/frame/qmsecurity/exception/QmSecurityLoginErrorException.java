package com.qm.frame.qmsecurity.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/6 21:50
 * @Description 登录异常
 */
public class QmSecurityLoginErrorException extends Exception {

    public QmSecurityLoginErrorException(String message) {
        super(message);
    }

    public QmSecurityLoginErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
