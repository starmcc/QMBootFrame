package com.qm.frame.basic.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/4/19 12:34
 * @Description 参数为空时触发该异常！
 */
public class QmParamNullException extends RuntimeException {

    public QmParamNullException() {
        super();
    }

    public QmParamNullException(String message) {
        super(message);
    }

    public QmParamNullException(Throwable cause) {
        super(cause);
    }

    ;

    public QmParamNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
