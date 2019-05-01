package com.qm.frame.mybatis.base;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/13 16:06
 * @Description QmBase错误异常
 */
public class QmBaseException extends RuntimeException {
    // 提供无参数的构造方法
    public QmBaseException() {
    }


    // 提供一个有参数的构造方法，可自动生成
    public QmBaseException(String message) {
        super(message);// 把参数传递给Throwable的带String参数的构造方法
    }

    // 提供一个带参的构造方法，并且输出一些异常信息
    public QmBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
