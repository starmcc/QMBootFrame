package com.qm.frame.qmsecurity.qmbject;

import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityCacheException;
import com.qm.frame.qmsecurity.exception.QmSecurityCreateTokenException;
import com.qm.frame.qmsecurity.exception.QmSecurityQmUserInfoException;
import com.qm.frame.qmsecurity.utils.QmSecurityTokenTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 16:59
 * @Description QmSecurity核心工具类
 */
public class QmSecurityManager implements Qmbject {

    // 日志工具
    private static final Logger LOG = LoggerFactory.getLogger(QmSecurityManager.class);

    // request
    private HttpServletRequest request;


    /**
     * 注入相关Spring依赖
     */
    public QmSecurityManager() {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
    }

    /**
     * 获取QmBject实例
     *
     * @return
     */
    public static Qmbject getQmbject() {
        return new QmSecurityManager();
    }


    @Override
    public String login(QmUserInfo qmUserInfo)
            throws QmSecurityQmUserInfoException, QmSecurityCreateTokenException, QmSecurityCacheException {
        // 判断是否存在必须的参数信息
        if (!verifyQmUserInfo(qmUserInfo)) {
            throw new QmSecurityQmUserInfoException("请检查qmUserInfo的参数是否完整！");
        }
        // 创建token
        try {
            String token = QmSecurityTokenTools.createToken(qmUserInfo);
            qmUserInfo.setToken(token);
        } catch (Exception e) {
            throw new QmSecurityCreateTokenException("签发token未知错误!", e);
        }
        // 缓存用户对象
        try {
            QmSecurityContent.qmSecurityCache.put("Qmbject_" + qmUserInfo.getIdentify(),
                    qmUserInfo, qmUserInfo.getLoginExpireTime());
        } catch (Exception e) {
            throw new QmSecurityCacheException(e);
        }
        return qmUserInfo.getToken();
    }


    @Override
    public QmUserInfo getUserInfo() {
        try {
            return (QmUserInfo) request.getAttribute(QmUserInfo.class.getName());
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void setUserInfo(QmUserInfo qmUserInfo) {
        String identify = qmUserInfo.getIdentify();
        QmSecurityContent.qmSecurityCache.put("Qmbject_" + identify, qmUserInfo, qmUserInfo.getLoginExpireTime());
    }


    @Override
    public List<String> extractMatchingUrls(boolean isNew) {
        QmUserInfo qmUserInfo = this.getUserInfo();
        // 首先获取当前用户的许可URI
        List<String> matchingUrls = (List<String>) QmSecurityContent.qmSecurityCache.get("matchingUrls_" + qmUserInfo.getIdentify());
        if (isNew || matchingUrls == null) {
            matchingUrls = QmSecurityContent.realm.authorizationPermissions(qmUserInfo);
            // 半小时缓存时间
            QmSecurityContent.qmSecurityCache.put("matchingUrls_" + qmUserInfo.getIdentify(), matchingUrls, 60 * 30);
        }
        return matchingUrls;
    }

    /**
     * 校验登录对象是否完整
     *
     * @param qmUserInfo
     * @return
     */
    private boolean verifyQmUserInfo(QmUserInfo qmUserInfo) {
        if (qmUserInfo == null) return false;
        if (StringUtils.isEmpty(qmUserInfo.getIdentify())) return false;
        if (qmUserInfo.getUser() == null) return false;
        return true;
    }

    @Override
    public boolean logout(String identify) {
        if (StringUtils.isEmpty(identify)) return false;
        if (!QmSecurityContent.qmSecurityCache.remove("Qmbject_" + identify)) {
            return false;
        }
        return QmSecurityContent.qmSecurityCache.remove("matchingUrls_" + identify);
    }
}
