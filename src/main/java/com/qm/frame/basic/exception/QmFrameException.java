package com.qm.frame.basic.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/30 19:46
 * @Description 框架异常
 */
public class QmFrameException extends RuntimeException{


    public QmFrameException() {
        super();
    }

    public QmFrameException(String message) {
        super(message);
    }


    public QmFrameException(String message, Throwable cause) {
        super(message, cause);
    }
}
