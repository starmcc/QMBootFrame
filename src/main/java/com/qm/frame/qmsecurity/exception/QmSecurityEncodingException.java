package com.qm.frame.qmsecurity.exception;

import java.io.IOException;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:12
 * @Description
 */
public class QmSecurityEncodingException extends RuntimeException {

    public QmSecurityEncodingException(String message) {
        super(message);
    }

    public QmSecurityEncodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
