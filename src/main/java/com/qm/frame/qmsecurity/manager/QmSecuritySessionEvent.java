package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.entity.QmSessionInfo;

import javax.servlet.http.HttpSessionBindingEvent;
import java.util.List;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/2/12 3:15
 * @Description 使用session校验机制时的session事件机制回调接口
 */
public interface QmSecuritySessionEvent {

    /**
     * 用户失效,过期,注销时触发该方法回调
     * @param qmSessionInfo 用户session保存对象
     * @param qmSessionInfoList 所有用户集合
     * @param event 触发事件对象
     */
    void loginUserSessionEvent(QmSessionInfo qmSessionInfo,
                               List<QmSessionInfo> qmSessionInfoList,
                               HttpSessionBindingEvent event);

}
