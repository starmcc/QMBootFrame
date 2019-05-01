package com.qm.frame.basic.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/4/19 12:34
 * @Description 参数解析错误时触发该异常!
 */
public class QmParamErrorException extends RuntimeException {

    public QmParamErrorException() {
        super();
    }

    public QmParamErrorException(String message) {
        super(message);
    }

    public QmParamErrorException(Throwable cause) {
        super(cause);
    }

    ;

    public QmParamErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
