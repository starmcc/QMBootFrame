package com.qm.frame.qmsecurity.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.qm.frame.basic.util.HttpApiUtil;
import com.qm.frame.basic.util.QmRedisClient;
import com.qm.frame.basic.util.QmSpringManager;
import com.qm.frame.qmsecurity.basic.QmSecurityAESUtil;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmPermissions;
import com.qm.frame.qmsecurity.entity.QmSessionInfo;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

    // request
    private HttpServletRequest request;
    // 依赖Servlet的全局上下文作用域
    private ServletContext application;
    // 依赖配置类
    private QmSecurityContent qmSecurityContent;


    /**
     * 注入相关Spring依赖
     */
    public QmSecurityManager() {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        if (application == null) {
            application = request.getSession().getServletContext();
        }
        try {
            qmSecurityContent = QmSpringManager.getBean(QmSecurityContent.class);
        } catch (Exception e) {
            return;
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
    public String login(final QmTokenInfo qmTokenInfo, final long expireTime) {
        // 判断是否存在必须的参数信息
        if (StringUtils.isEmpty(qmTokenInfo.getUserName())
                || expireTime <= 0) {
            new Exception("请检查qmTokenInfo的参数是否完整！");
            return null;
        }

        JWTCreator.Builder builder = JWT.create();
        // 创建JWT Helder部分
        Map<String, Object> headerClaims = new HashMap<String, Object>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");
        builder.withHeader(headerClaims);
        // 创建主体信息
        builder.withClaim("qm_security_userName", qmTokenInfo.getUserName());
        builder.withClaim("qm_security_roleId", qmTokenInfo.getRoleId());
        // 封装ip
        String requestIp = HttpApiUtil.getHttpIp(request);
        builder.withClaim("qm_security_requestIp", requestIp);
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
        builder.withExpiresAt(new Date(currentTimeMillis + expireTime * 1000));
        // 签发时间 当前时间
        builder.withIssuedAt(new Date(currentTimeMillis));
        try {
            // 将这些信息生成token签名
            String token = builder.sign(Algorithm.HMAC256(qmSecurityContent.getTokenSecret()));
            // AES加密手段
            token = QmSecurityAESUtil.encryptAES(token);
            return token;
        } catch (Exception e) {
            System.out.println("签名错误");
        }
        return null;
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
        List<String> matchUrls = qmSecurityContent.getQmSecurityRealm().authorizationPermissions(roleId);
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
        if (qmSecurityContent.isStartRedis()) {
            return (List<QmPermissions>) QmRedisClient.get(QM_PERMISSIONS_KEY);
        }
        // 使用application进行缓存
        return (List<QmPermissions>) application.getAttribute(QM_PERMISSIONS_KEY);
    }

    /**
     * 从缓存设置权限列表
     *
     * @param qmPermissionsList
     * @return
     */
    private void setCacheQmPermissions(List<QmPermissions> qmPermissionsList) {
        // 是否开启Redis管理权限
        if (qmSecurityContent.isStartRedis()) {
            try {
                QmRedisClient.set(QM_PERMISSIONS_KEY, qmPermissionsList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 使用application进行缓存
        application.setAttribute(QM_PERMISSIONS_KEY, qmPermissionsList);
    }


    @Override
    public QmTokenInfo getTokenInfo() {
        try {
            return (QmTokenInfo) request.getAttribute(QmTokenInfo.class.getName());
        } catch (Exception e) {
            return null;
        }
    }

}
