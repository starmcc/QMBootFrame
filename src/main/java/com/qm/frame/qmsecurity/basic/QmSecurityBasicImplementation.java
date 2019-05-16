package com.qm.frame.qmsecurity.basic;

import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityAnalysisTokenException;
import com.qm.frame.qmsecurity.exception.QmSecurityCreateTokenException;
import com.qm.frame.qmsecurity.qmbject.QmSecurityManager;
import com.qm.frame.qmsecurity.utils.QmSecurityTokenTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static final String USER_KEY = "Qmbject_";

    @Override
    public boolean securityCheck(HttpServletRequest request, HttpServletResponse response, boolean isPerssions)
            throws Exception {
        // 从头部获取token字段
        String token = request.getHeader(QmSecurityContent.headerTokenKeyName);
        // 如果为空则直接拦截
        if (token == null) {
            LOG.info("※获取token失败,拒绝访问※");
            QmSecurityContent.realm.noPassCallBack(request, response, 1);
            return false;
        }
        QmUserInfo qmUserInfo = null;
        try {
            LOG.info("※分析并提取token信息※");
            qmUserInfo = QmSecurityTokenTools.analysisToken(token);
        } catch (QmSecurityAnalysisTokenException e) {
            LOG.info("※提取token失败※");
            QmSecurityContent.realm.noPassCallBack(request, response, 2);
            return false;
        }
        // 从缓存中获取该用户的登录信息。
        qmUserInfo = (QmUserInfo) QmSecurityContent.qmSecurityCache.get(USER_KEY + qmUserInfo.getIdentify());
        if (qmUserInfo == null || !token.equals(qmUserInfo.getToken())) {
            LOG.info("※用户登录已过期※");
            QmSecurityContent.realm.noPassCallBack(request, response, 3);
            return false;
        }
        LOG.info("※验证token是否过期※");
        long exp = qmUserInfo.getTokenExpireTime();
        long signTime = qmUserInfo.getSignTime().getTime();
        if (!QmSecurityTokenTools.verifyTokenExp(exp, signTime)) {
            try {
                // 尝试重新签发token
                LOG.info("※尝试重新签发token※");
                qmUserInfo = restartAuth(response, qmUserInfo);
                LOG.info("※重新签发token成功※");
            } catch (QmSecurityCreateTokenException e) {
                e.printStackTrace();
                // 重新授权时发生异常
                // 发生该异常的情况有以下情况：
                // 1.qmuserinfo参数不完整,2.签发token失败,3.缓存异常!
                QmSecurityContent.realm.noPassCallBack(request, response, 4);
                return false;
            }
        }
        // 提供给调度者授权调用,可修改用户对象信息。
        // 如果返回空,则认为授权失败！否则需调度者返回实质上的用户对象。
        LOG.info("※进入授权验证※");
        qmUserInfo = QmSecurityContent.realm.authorizationUserInfo(request, response, qmUserInfo);
        if (qmUserInfo == null) {
            QmSecurityContent.realm.noPassCallBack(request, response, 5);
            return false;
        }
        LOG.info("※通过授权验证※");
        // 保存到作用域中提供框架调用
        request.setAttribute(QmUserInfo.class.getName(), qmUserInfo);
        // 缓存 token 活跃期
        QmSecurityContent.qmSecurityCache.put(USER_KEY + qmUserInfo.getIdentify(),
                qmUserInfo, qmUserInfo.getLoginExpireTime());
        // 该判断为如果标注了@QmPass且needLogin为true时，则isPerssions为false，就不会进入授权匹配了。
        if (isPerssions) {
            LOG.info("※正在进行授权URI验证※");
            // 获取该角色的权限信息
            List<String> matchingUrls = QmSecurityManager.getQmbject().extractMatchingURI(false);
            // 获取请求路由 校验该角色是否存在匹配当前请求url的匹配规则。
            boolean is = this.verifyMatchingURI(request.getServletPath(), matchingUrls);
            if (!is) {
                LOG.info("※权限不足,拒绝访问※");
                QmSecurityContent.realm.noPassCallBack(request, response, 6);
                return false;
            }
        }
        LOG.info("※※用户已通过安全框架验证※※");
        return true;
    }

    /**
     * Spring提供的模糊路径匹配算法
     * 匹配角色授权URI
     *
     * @param matchingUrl 匹配路径
     * @param requestUrl  请求地址
     * @return
     */
    public boolean verifyMatchingURI(String requestUri, List<String> matchingUris) {
        for (String matchingUri : matchingUris) {
            // Spring提供的模糊路径匹配算法
            PathMatcher matcher = new AntPathMatcher();
            if (matcher.match(matchingUri, requestUri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重新签发token
     *
     * @param token
     * @return
     */
    private QmUserInfo restartAuth(HttpServletResponse response, QmUserInfo qmUserInfo) throws Exception {
        // 先删除该缓存的token值
        QmSecurityContent.qmSecurityCache.remove(USER_KEY + qmUserInfo.getIdentify());
        // 进行重新签发token
        String token = QmSecurityTokenTools.createToken(qmUserInfo);
        // headers放行获取token的key
        String headerKey = "Access-Control-Expose-Headers";
        String typeName = "Content-Type," + QmSecurityContent.headerTokenKeyName;
        response.addHeader(headerKey, typeName);
        // 保存到响应头response的headers中
        response.setHeader(QmSecurityContent.headerTokenKeyName, token);
        // 替换新的token值
        qmUserInfo.setToken(token);
        return qmUserInfo;
    }

}
