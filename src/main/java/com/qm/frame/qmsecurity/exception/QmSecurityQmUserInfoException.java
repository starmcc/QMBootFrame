package com.qm.frame.qmsecurity.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/6 21:50
 * @Description qmUserInfo的参数不完整
 */
public class QmSecurityQmUserInfoException extends Exception {

    private static final String MSG = "请检查qmUserInfo的参数是否完整！";

    public QmSecurityQmUserInfoException() {
        super(MSG);
    }

    public QmSecurityQmUserInfoException(Throwable cause) {
        super(MSG, cause);
    }
}
