package com.qm.frame.qmsecurity.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:12
 * @Description 签发token时系统错误
 */
public class QmSecurityCreateTokenException extends Exception {

    public QmSecurityCreateTokenException() {
        super();
    }

    public QmSecurityCreateTokenException(String message) {
        super(message);
    }

    public QmSecurityCreateTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public QmSecurityCreateTokenException(Throwable cause) {
        super(cause);
    }
}
