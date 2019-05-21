package com.qm.frame.qmsecurity.basic;

import com.qm.frame.qmsecurity.config.QmSecurityConstants;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.entity.TokenContainer;
import com.qm.frame.qmsecurity.exception.QmSecurityAnalysisTokenException;
import com.qm.frame.qmsecurity.qmbject.QmSecurityManager;
import com.qm.frame.qmsecurity.utils.QmSecurityTokenTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Copyright © 2019浅梦工作室. All rights reserved.
 *
 * @author 浅梦
 * @date 2019/5/12 0:42
 * @Description QmSecurity安全框架底层实现
 */
public class QmSecurityBasicImplementation implements QmSecurityBasic {

    private static final Logger LOG = LoggerFactory.getLogger(QmSecurityBasicImplementation.class);

    @Override
    public boolean securityCheck(HttpServletRequest request, HttpServletResponse response, boolean isPerssions)
            throws Exception {
        // 从头部获取token字段 如果为空则直接拦截
        String token = request.getHeader(QmSecurityContent.headerTokenKeyName);
        if (token == null) {
            LOG.info("※获取token失败,拒绝访问※");
            QmSecurityContent.realm.noPassCallBack(1, request, response);
            return false;
        }
        String identify = null;
        try {
            LOG.info("※分析并提取token信息※");
            identify = QmSecurityTokenTools.analysisToken(token);
        } catch (QmSecurityAnalysisTokenException e) {
            LOG.info("※提取token失败※");
            QmSecurityContent.realm.noPassCallBack(2, request, response);
            return false;
        }
        // 从缓存中获取该用户的登录信息。
        QmUserInfo qmUserInfo = (QmUserInfo)
                QmSecurityContent.qmSecurityCache.get(QmSecurityConstants.USER_KEY + identify);
        if (qmUserInfo == null) {
            LOG.info("※用户登录已过期※");
            QmSecurityContent.realm.noPassCallBack(3, request, response);
            return false;
        }
        // 从缓存中获取token签发容器。
        TokenContainer tokenContainer = QmSecurityTokenTools.getTokenContainer(qmUserInfo.getIdentify());
        if (!token.equals(qmUserInfo.getToken()) && tokenContainer == null) {
            // 1.如果请求token和缓存token不一致时进入该层
            // 2.寻找token签发容器是否存在
            // 3. 不存在则认为该用户的token已过期
            if (qmUserInfo.isReplaceLogin()) {
                LOG.info("※用户在其他地方进行登录※");
                QmSecurityContent.realm.noPassCallBack(4, request, response);
                return false;
            }else {
                LOG.info("※用户token已过期※");
                QmSecurityContent.realm.noPassCallBack(5, request, response);
                return false;
            }

        }
        // token过期时进行重新签发token
        if (!QmSecurityTokenTools.verifyExp(
                qmUserInfo.getTokenExpireTime(),
                qmUserInfo.getSignTime().getTime())) {
            // ===================token已过期进入==================
            if (tokenContainer == null) {
                LOG.info("※尝试重新签发token※");
                qmUserInfo.setSignTime(new Date());
                String newToken = QmSecurityTokenTools.restartAuth(qmUserInfo);
                qmUserInfo.setToken(newToken);
                QmSecurityTokenTools.insertTokenContainer(token, newToken, qmUserInfo.getIdentify());
                LOG.info("※重新签发token成功※");
            }
            QmSecurityTokenTools.sendResponseToken(response, tokenContainer.getNewToken());
        }
        // 提供给调度者授权调用,可修改用户对象信息。如果返回空,则认为授权失败！否则需调度者返回实质上的用户对象。
        LOG.info("※进入授权验证※");
        qmUserInfo = QmSecurityContent.realm.authorizationUserInfo(qmUserInfo, request, response);
        if (qmUserInfo == null) {
            LOG.info("※授权验证拦截※");
            QmSecurityContent.realm.noPassCallBack(6, request, response);
            return false;
        }
        LOG.info("※通过授权验证※");
        // 保存到作用域中提供框架调用
        request.setAttribute(QmUserInfo.class.getName(), qmUserInfo);
        // 缓存 token 活跃期
        QmSecurityContent.qmSecurityCache.put(
                QmSecurityConstants.USER_KEY + qmUserInfo.getIdentify(),
                qmUserInfo, qmUserInfo.getLoginExpireTime());
        // 该判断为如果标注了@QmPass且needLogin为true时，则isPerssions为false，就不会进入授权匹配了。
        if (isPerssions) {
            if (!this.verifyPermission(request)) {
                LOG.info("※权限不足,拒绝访问※");
                QmSecurityContent.realm.noPassCallBack(7, request, response);
                return false;
            }
        }
        LOG.info("※※用户已通过安全框架验证※※");
        return true;
    }

    /**
     * 校验用户权限
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    private boolean verifyPermission(HttpServletRequest request) {
        LOG.info("※正在进行授权URI验证※");
        // 获取该角色的权限信息
        List<String> matchingUrls = QmSecurityManager.getQmbject().extractMatchingURI(false);
        // 获取请求路由 校验该角色是否存在匹配当前请求url的匹配规则。
        return this.verifyMatchingURI(request.getServletPath(), matchingUrls);
    }

    /**
     * Spring提供的模糊路径匹配算法
     * 匹配角色授权URI
     *
     * @param matchingUrl 匹配路径
     * @param requestUrl  请求地址
     * @return
     */
    private boolean verifyMatchingURI(String requestUri, List<String> matchingUris) {
        for (String matchingUri : matchingUris) {
            // Spring提供的模糊路径匹配算法
            PathMatcher matcher = new AntPathMatcher();
            if (matcher.match(matchingUri, requestUri)) {
                return true;
            }
        }
        return false;
    }


}
