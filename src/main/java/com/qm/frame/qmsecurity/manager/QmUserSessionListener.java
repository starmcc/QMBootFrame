package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmSessionInfo;
import com.qm.frame.qmsecurity.util.QmSecuritySpringMapnager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.ArrayList;
import java.util.List;

/**
 * BindingListener session感知监听器
 * 注释时间：2019/2/12 3:25
 */
public class QmUserSessionListener implements HttpSessionBindingListener {

    /**
     * 用户对象
     */
    private QmSessionInfo qmSessionInfo;
    /**
     * 事件回调接口
     */
    private QmSecuritySessionEvent qmSecuritySessionEvent;

    /**
     * 初始化该监听回调接口
     */
    public QmUserSessionListener() {
        QmSecurityContent qmSecurityContent = QmSecuritySpringMapnager.getBean(QmSecurityContent.class);
        this.qmSecuritySessionEvent = qmSecurityContent.getQmSecuritySessionEvent();
    }

    /**
     * 获取用户对象
     *
     * @return
     */
    public QmSessionInfo getQmSessionInfo() {
        return qmSessionInfo;
    }

    /**
     * 保存用户对象
     *
     * @param qmSessionInfo
     */
    public void setQmSessionInfo(QmSessionInfo qmSessionInfo) {
        this.qmSessionInfo = qmSessionInfo;
    }

    /**
     * session 绑定时触发
     *
     * @param event
     */
    public void valueBound(HttpSessionBindingEvent event) {
        // 绑定时触发
        //--------------------业务处理------------------
        ServletContext application = event.getSession().getServletContext();
        List<QmSessionInfo> qmSessionInfoList = (List<QmSessionInfo>) application.getAttribute("userList");
        if (qmSessionInfoList == null) {
            qmSessionInfoList = new ArrayList<QmSessionInfo>();
        }
        qmSessionInfoList.add(qmSessionInfo);
        application.setAttribute("qmSessionInfoList", qmSessionInfoList);
        //------------------业务处理---------------------
    }

    /**
     * session 解绑时触发
     *
     * @param event
     */
    public void valueUnbound(HttpSessionBindingEvent event) {
        // 解绑时触发
        //------------------业务处理---------------------
        ServletContext application = event.getSession().getServletContext();
        List<QmSessionInfo> qmSessionInfoList = (List<QmSessionInfo>) application.getAttribute("userList");
        if (qmSessionInfoList != null) {
            qmSessionInfoList.remove(qmSessionInfo);
        }
        application.setAttribute("qmSessionInfoList", qmSessionInfoList);
        //------------------业务处理---------------------
        if (qmSecuritySessionEvent != null) {
            qmSecuritySessionEvent.loginUserSessionEvent(qmSessionInfo, qmSessionInfoList, event);
        }
    }
}