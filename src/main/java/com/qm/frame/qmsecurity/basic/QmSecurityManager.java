package com.qm.frame.qmsecurity.basic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.qm.frame.basic.util.HttpApiUtil;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmPermissions;
import com.qm.frame.qmsecurity.entity.QmSessionInfo;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import com.qm.frame.qmsecurity.exception.QmSecuritySignTokenException;
import com.qm.frame.qmsecurity.manager.QmUserSessionListener;
import com.qm.frame.qmsecurity.manager.Qmbject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 16:59
 * @Description QmSecurity核心工具类
 */
public class QmSecurityManager implements Qmbject {

    // 依赖ServletContext进行全局缓存，key名
    private static final String QM_PERMISSIONS_KEY = "QmSecurityManager_QmPermissions";
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
    public String login(QmTokenInfo qmTokenInfo) throws QmSecuritySignTokenException {
        // 判断是否存在必须的参数信息
        if (StringUtils.isEmpty(qmTokenInfo.getIdentify()) || qmTokenInfo.getExpireTime() <= 0) {
            throw new QmSecuritySignTokenException("请检查qmTokenInfo的参数是否完整！");
        }
        JWTCreator.Builder builder = JWT.create();
        // 创建JWT Helder部分
        Map<String, Object> headerClaims = new HashMap<String, Object>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");
        builder.withHeader(headerClaims);
        // 创建主体信息
        builder.withClaim("qm_security_identify", qmTokenInfo.getIdentify());
        builder.withClaim("qm_security_roleId", qmTokenInfo.getRoleId());
        // 封装ip
        String requestIp = HttpApiUtil.getHttpIp(request);
        builder.withClaim("qm_security_requestIp", requestIp);
        builder.withClaim("qm_security_expireTime", qmTokenInfo.getExpireTime());
        // 封装自定义信息
        Map<String, String> infoMap = qmTokenInfo.getInfoMap();
        if (infoMap != null) {
            for (String key : infoMap.keySet()) {
                builder.withClaim(key, infoMap.get(key));
            }
        }
        // 获取当前时间的unix时间戳
        long currentTimeMillis = System.currentTimeMillis();
        // 设置token过期时间，毫秒 * 1000 + 当前时间戳
        builder.withExpiresAt(new Date(currentTimeMillis + (qmTokenInfo.getExpireTime() * 1000)));
        // 签发时间 当前时间
        builder.withIssuedAt(new Date(currentTimeMillis));
        try {
            // 将这些信息生成token签名
            String token = builder.sign(Algorithm.HMAC256(QmSecurityContent.tokenSecret));
            // AES加密手段
            token = QmSecurityAesTools.encryptAES(token);
            QmSecurityContent.redisclient.set("Token_" + token, qmTokenInfo, qmTokenInfo.getTokenActiveTime());
            return token;
        } catch (Exception e) {
            throw new QmSecuritySignTokenException("签名错误！", e);
        }
    }


    @Override
    public void loginForSession(QmSessionInfo qmSessionInfo, int expireTime) {
        HttpSession session = request.getSession();
        QmUserSessionListener qmUserSessionListener = new QmUserSessionListener();
        qmUserSessionListener.setQmSessionInfo(qmSessionInfo);
        session.setAttribute("userCountListener", qmUserSessionListener);
        session.setMaxInactiveInterval(expireTime);
    }

    @Override
    public QmSessionInfo getSessionInfo() {
        try {
            return (QmSessionInfo) request.getAttribute(QmSessionInfo.class.getName());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setSessionInfo(QmSessionInfo user) {
        HttpSession session = request.getSession();
        QmUserSessionListener qmUserSessionListener = new QmUserSessionListener();
        qmUserSessionListener.setQmSessionInfo(user);
        session.setAttribute("userCountListener", qmUserSessionListener);
    }


    @Override
    public QmPermissions extractQmPermissions(int roleId, boolean isNew) {
        // 首先调用获取所有的角色权限列表
        List<QmPermissions> QmPermissionsLis = this.getAllQmPermissions();
        // 外部定义qmPermissions
        QmPermissions qmPermissions = null;
        // 遍历查找该roleId的对象并返回
        for (int i = 0; i < QmPermissionsLis.size(); i++) {
            // 遍历该角色的id是否一致
            if (roleId == QmPermissionsLis.get(i).getRoleId()) {
                qmPermissions = QmPermissionsLis.get(i);
                if (isNew) {
                    // 如果是真，则肯定会更新信息。
                    qmPermissions = this.updateRoleQmPermissionsPack(roleId);
                    // 替换该对象,将该对象保存起来
                    QmPermissionsLis.set(i, qmPermissions);
                    this.setCacheQmPermissions(QmPermissionsLis);
                }
                break;
            }
        }
        // 如果是空的则表示在缓存中找不到该角色信息，也使用调用者提供的方法去获取
        if (qmPermissions == null) {
            qmPermissions = this.updateRoleQmPermissionsPack(roleId);
            QmPermissionsLis.add(qmPermissions);
            this.setCacheQmPermissions(QmPermissionsLis);
        }
        return qmPermissions;
    }

    /**
     * 更新角色权限信息
     *
     * @param roleId
     * @return
     */
    private QmPermissions updateRoleQmPermissionsPack(int roleId) {
        QmPermissions qmPermissions = new QmPermissions();
        List<String> matchUrls = QmSecurityContent.realm.authorizationPermissions(roleId);
        qmPermissions.setRoleId(roleId);
        qmPermissions.setMatchUrls(matchUrls);
        return qmPermissions;
    }

    @Override
    public List<QmPermissions> getAllQmPermissions() {
        List<QmPermissions> QmPermissionsLis = null;
        try {
            // 从ServletContext全局缓存中获取到该角色权限集合对象
            QmPermissionsLis = getCacheQmPermissions();
            if (QmPermissionsLis == null) {
                // 如果是空的，则创建一个新的对象作为返回
                QmPermissionsLis = new ArrayList<>();
            }
        } catch (Exception e) {
            // 同样的，如果错误的话，也创建一个新的对象作为返回
            QmPermissionsLis = new ArrayList<>();
        }
        return QmPermissionsLis;
    }

    /**
     * 从缓存获取权限列表
     *
     * @return
     */
    private List<QmPermissions> getCacheQmPermissions() {
        // 是否开启Redis管理权限
        return (List<QmPermissions>) QmSecurityContent.redisclient.get(QM_PERMISSIONS_KEY);
    }

    /**
     * 从缓存设置权限列表
     *
     * @param qmPermissionsList
     * @return
     */
    private void setCacheQmPermissions(List<QmPermissions> qmPermissionsList) {
        // 是否开启Redis管理权限
        try {
            QmSecurityContent.redisclient.set(QM_PERMISSIONS_KEY, qmPermissionsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public QmTokenInfo getTokenInfo() {
        try {
            return (QmTokenInfo) request.getAttribute(QmTokenInfo.class.getName());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean verifyToken(HttpServletRequest request, HttpServletResponse response, boolean isPerssions) {
        // 校验类型
        String typeName = QmSecurityContent.sessionOrToken;
        // 角色id
        Integer roleId;
        if (typeName.trim().equalsIgnoreCase("session")) {
            // 从session获取用户对象
            HttpSession session = request.getSession();
            QmUserSessionListener qmUserSessionListener = (QmUserSessionListener) session.getAttribute("qmUserSessionListener");
            if (qmUserSessionListener == null) {
                LOG.info("※找不到用户信息,登录超时※");
                QmSecurityContent.realm.noPassCallBack(request, response, 1);
                return false;
            } else {
                QmSessionInfo qmSessionInfo = qmUserSessionListener.getQmSessionInfo();
                // 保存到作用域中提供直接缓存
                request.setAttribute(QmUserSessionListener.class.getName(), qmSessionInfo);
                roleId = qmSessionInfo.getRoleId();
            }
        } else {
            // 从头部获取token字段
            String token = request.getHeader(QmSecurityContent.headerTokenKeyName);
            // 如果为空则直接拦截
            if (token == null) {
                LOG.info("※检测不到token拒绝访问※");
                QmSecurityContent.realm.noPassCallBack(request, response, 2);
                return false;
            }
            LOG.info("※正在验证Token是否正确※");
            QmTokenInfo qmTokenInfo = QmSecurityBasic.verifyToken(token);
            if (qmTokenInfo == null) {
                // 尝试重新签发token
                LOG.info("※正在尝试重新签发Token※");
                qmTokenInfo = restartAuth(response, token);
                // 重新签发token失败
                if (qmTokenInfo == null) {
                    LOG.info("※Token失效或已过期※");
                    QmSecurityContent.realm.noPassCallBack(request, response, 3);
                    return false;
                }
            } else {
                LOG.info("※进行请求Ip单点匹配※");
                String requestIp = HttpApiUtil.getHttpIp(request);
                if (!requestIp.equals(qmTokenInfo.getRequestIp())) {
                    LOG.info("※请求ip校验失败※");
                    QmSecurityContent.realm.noPassCallBack(request, response, 4);
                    return false;
                }
            }
            roleId = qmTokenInfo.getRoleId();
            // 保存到作用域中提供直接缓存
            request.setAttribute(QmTokenInfo.class.getName(), qmTokenInfo);
            // 缓存 token 活跃期
            QmSecurityContent.redisclient.set("token_" + token, qmTokenInfo, qmTokenInfo.getTokenActiveTime());
        }
        // 该判断为如果标注了@QmPass且needLogin为true时，则isPerssions为false，就不会进入授权匹配了。
        if (isPerssions) {
            LOG.info("※正在进行授权访问匹配※");
            // 获取请求路由
            String path = request.getServletPath();
            // 获取该角色的权限信息
            QmPermissions qmPermissions = QmSecurityManager.getQmbject().extractQmPermissions(roleId, false);
            // 校验该角色是否存在匹配当前请求url的匹配规则。
            boolean is = QmSecurityBasic.verifyPermissions(path, qmPermissions.getMatchUrls());
            if (!is) {
                LOG.info("※权限不足,拒绝访问※");
                QmSecurityContent.realm.noPassCallBack(request, response, 5);
                return false;
            }
        }
        LOG.info("※用户通过安全校验※");
        return true;
    }

    /**
     * 重新签发token
     *
     * @param token
     * @return
     */
    private QmTokenInfo restartAuth(HttpServletResponse response, String token) {
        try {
            // 查看缓存中是否存在该对象qmTokenInfo
            Object obj = QmSecurityContent.redisclient.get("token_" + token);
            // 如果找不到证明不活跃已过期
            if (obj == null) return null;
            QmTokenInfo qmTokenInfo = (QmTokenInfo) obj;
            // 获取到qmTokenInfo对象后，进行重新登录
            token = QmSecurityManager.getQmbject().login(qmTokenInfo);
            if (token != null) {
                response.setHeader(QmSecurityContent.headerTokenKeyName, token);
                return qmTokenInfo;
            }
        } catch (QmSecuritySignTokenException e) {
            e.printStackTrace();
        }
        return null;
    }
}
