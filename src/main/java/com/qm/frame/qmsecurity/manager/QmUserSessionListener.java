package com.qm.frame.qmsecurity.manager;

import com.qm.frame.qmsecurity.entity.QmSessionInfo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.ArrayList;
import java.util.List;
/**
*    BindingListener实例类
*/
public class QmUserSessionListener implements HttpSessionBindingListener{

	private QmSessionInfo qmSessionInfo;

	public QmSessionInfo getQmSessionInfo() {
		return qmSessionInfo;
	}

	public void setQmSessionInfo(QmSessionInfo qmSessionInfo) {
		this.qmSessionInfo = qmSessionInfo;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		// 绑定时触发
        //--------------------业务处理------------------
        ServletContext application = event.getSession().getServletContext();
        List<QmSessionInfo> userList = (List<QmSessionInfo>)application.getAttribute("userList");
		if(userList == null){
            userList = new ArrayList<QmSessionInfo>();
        }
        userList.add(qmSessionInfo);
        application.setAttribute("userList",userList);
        //------------------业务处理---------------------
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		// 解绑时触发
          //------------------业务处理---------------------
		ServletContext application = event.getSession().getServletContext();
        List<QmSessionInfo> userList = (List<QmSessionInfo>)application.getAttribute("userList");
		if(userList != null){
            userList.remove(qmSessionInfo);
        }
        application.setAttribute("userList",userList);
          //------------------业务处理---------------------
	}
}