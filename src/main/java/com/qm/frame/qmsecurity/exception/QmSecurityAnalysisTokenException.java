package com.qm.frame.qmsecurity.exception;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/10 12:16
 * @Description 解密token失败！
 */
public class QmSecurityAnalysisTokenException extends Exception {

    private static final String MSG = "解密token失败！";

    public QmSecurityAnalysisTokenException() {
        super(MSG);
    }

    public QmSecurityAnalysisTokenException(Throwable cause) {
        super(MSG, cause);
    }
}
