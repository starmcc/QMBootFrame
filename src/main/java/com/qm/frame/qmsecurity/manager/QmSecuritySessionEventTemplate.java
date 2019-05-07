package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.entity.QmSessionInfo;

import javax.servlet.http.HttpSessionBindingEvent;
import java.util.List;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/7 9:52
 * @Description 使用session校验机制时的session事件机制回调接口默认模板
 */
public class QmSecuritySessionEventTemplate implements QmSecuritySessionEvent{


    @Override
    public void loginUserSessionEvent(QmSessionInfo qmSessionInfo, List<QmSessionInfo> qmSessionInfoList, HttpSessionBindingEvent event) {

    }
}
