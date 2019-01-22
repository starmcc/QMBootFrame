package com.qm.frame.mybatis.dto;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/1/13 3:31
 * @Description QmBaseDto异常
 */
public class QmBaseDtoException extends RuntimeException{

    // 提供无参数的构造方法
    public QmBaseDtoException() { }


    // 提供一个有参数的构造方法，可自动生成
    public QmBaseDtoException(String message) {
        super(message);// 把参数传递给Throwable的带String参数的构造方法
    }

    public QmBaseDtoException(String message, Throwable cause) {
        super(message, cause);
    }
}
