package com.qm.frame.qmsecurity.qmbject;

import com.qm.frame.qmsecurity.config.QmSecurityConstants;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityCreateTokenException;
import com.qm.frame.qmsecurity.exception.QmSecurityQmUserInfoException;
import com.qm.frame.qmsecurity.utils.QmSecurityTokenTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 16:59
 * @Description QmSecurity核心工具类
 */
public class QmSecurityManager implements Qmbject {

    /**
     * logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(QmSecurityManager.class);

    /**
     * rquest对象
     */
    private HttpServletRequest request;

    /**
     * 注入相关Spring依赖
     */
    private QmSecurityManager() {
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
            throws QmSecurityQmUserInfoException, QmSecurityCreateTokenException {
        // 判断是否存在必须的参数信息
        if (!verifyQmUserInfo(qmUserInfo)) {
            throw new QmSecurityQmUserInfoException();
        }
        QmUserInfo qmUserInfoCache = null;
        // 从缓存中获取该账号是否已登录
        qmUserInfoCache = (QmUserInfo) QmSecurityContent.qmSecurityCache.get(
                QmSecurityConstants.USER_KEY + qmUserInfo.getIdentify());
        // 从缓存中清空该账号权限
        QmSecurityContent.qmSecurityCache.remove(
                QmSecurityConstants.MATCHING_KEY + qmUserInfo.getIdentify());
        // 如果緩存中存在用户并判断是否为单点登录
        if (qmUserInfoCache != null && !qmUserInfoCache.isSingleSignOn()) {
            // 如果单点登录是false，则获取缓存中的token。
            return qmUserInfoCache.getToken();
        }
        // 创建token
        try {
            qmUserInfo.setSignTime(new Date());
            String token = QmSecurityTokenTools.createToken(qmUserInfo);
            qmUserInfo.setToken(token);
        } catch (Exception e) {
            throw new QmSecurityCreateTokenException(e);
        }
        // 缓存用户对象
        QmSecurityContent.qmSecurityCache.put(
                QmSecurityConstants.USER_KEY + qmUserInfo.getIdentify(),
                qmUserInfo, qmUserInfo.getLoginExpireTime());
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
        QmSecurityContent.qmSecurityCache.put(
                QmSecurityConstants.USER_KEY + identify,
                qmUserInfo, qmUserInfo.getLoginExpireTime());
    }

    @Override
    public QmUserInfo getUserInfo(String identify) {
        return (QmUserInfo) QmSecurityContent.qmSecurityCache.get(
                QmSecurityConstants.USER_KEY + identify);
    }

    @Override
    public List<String> extractMatchingURI(boolean isNew) {
        QmUserInfo qmUserInfo = this.getUserInfo();
        // 首先获取缓存中当前用户的许可URI
        List<String> matchingUrls = null;
        matchingUrls = (List<String>)
                QmSecurityContent.qmSecurityCache.get(
                        QmSecurityConstants.MATCHING_KEY + qmUserInfo.getIdentify());
        // 获取
        long matchUriExp = qmUserInfo.getMatchUriExpireTime();
        if (isNew || matchingUrls == null) {
            matchingUrls = QmSecurityContent.realm.authorizationMatchingURI(qmUserInfo);
            // 缓存权限URI
            QmSecurityContent.qmSecurityCache.put(
                    QmSecurityConstants.MATCHING_KEY + qmUserInfo.getIdentify(),
                    matchingUrls, matchUriExp);
        }
        return matchingUrls;
    }

    @Override
    public List<String> extractMatchingURI(String identify, boolean isNew) {
        QmUserInfo qmUserInfo = this.getUserInfo(identify);
        // 首先获取缓存中当前用户的许可URI
        List<String> matchingUrls = null;
        matchingUrls = (List<String>) QmSecurityContent.qmSecurityCache.get(
                QmSecurityConstants.MATCHING_KEY + qmUserInfo.getIdentify());
        // 获取
        long matchUriExp = qmUserInfo.getMatchUriExpireTime();
        if (isNew || matchingUrls == null) {
            matchingUrls = QmSecurityContent.realm.authorizationMatchingURI(qmUserInfo);
            // 缓存权限URI
            QmSecurityContent.qmSecurityCache.put(
                    QmSecurityConstants.MATCHING_KEY + qmUserInfo.getIdentify(),
                    matchingUrls, matchUriExp);
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
        if (qmUserInfo == null) {
            return false;
        }
        if ("".equals(qmUserInfo.getIdentify())) {
            return false;
        }
        if (qmUserInfo.getUser() == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean logout(String identify) {
        if ("".equals(identify)) {
            return false;
        }
        if (!QmSecurityContent.qmSecurityCache.remove(
                QmSecurityConstants.USER_KEY + identify)) {
            return false;
        }
        return QmSecurityContent.qmSecurityCache.remove(
                QmSecurityConstants.MATCHING_KEY + identify);
    }
}
