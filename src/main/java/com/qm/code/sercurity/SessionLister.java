package com.qm.code.sercurity;

import com.qm.frame.qmsecurity.entity.QmSessionInfo;
import com.qm.frame.qmsecurity.manager.QmSecuritySessionEvent;

import javax.servlet.http.HttpSessionBindingEvent;
import java.util.List;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 * @author 浅梦
 * @date 2019/2/12 3:39
 * @Description 当使用session校验机制时,可实现该事件监听接口，当session销毁或过期时，内部自动调用该接口的loginUserSessionEvent方法。
 */
public class SessionLister implements QmSecuritySessionEvent {
    @Override
    public void loginUserSessionEvent(QmSessionInfo qmSessionInfo,
                                      List<QmSessionInfo> qmSessionInfoList,
                                      HttpSessionBindingEvent event) {
        // 逻辑处理..
    }
}
