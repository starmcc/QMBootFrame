package com.qm.frame.qmsecurity.basic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.qm.frame.basic.util.HttpApiUtil;
import com.qm.frame.qmsecurity.config.QmSercurityContent;
import com.qm.frame.qmsecurity.entity.QmPermissions;
import com.qm.frame.qmsecurity.entity.QmTokenInfo;
import com.qm.frame.qmsecurity.manager.Qmbject;
import com.qm.frame.qmsecurity.manager.QmSecurityManager;
import com.qm.frame.qmsecurity.util.QmSecurityAESUtil;
import com.qm.frame.qmsecurity.util.QmSecuritySpringApplication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/22 16:59
 * @Description QmSecurity核心工具类
 */
@Deprecated
public class QmSecurityUtils implements Qmbject {

    // 依赖ServletContext进行全局缓存，key名
    private static final String QM_PERMISSIONS_KEY = "QmSecurityManager_QmPermissions";

    // request
    private HttpServletRequest request;
    // 依赖Servlet的全局上下文作用域
    private ServletContext application;
    /**
     * 注入相关Spring依赖
     */
    public QmSecurityUtils(){
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        if (application == null) {
            application = request.getSession().getServletContext();
        }
    }

    /**
     * 获取QmBject实例
     * @return
     */
    public static Qmbject getQmbject() {
        return new QmSecurityManager();
    }

    @Override
    public String login(final QmTokenInfo qmTokenInfo,final long expireTime) {
        // 判断是否存在必须的参数信息
        if (StringUtils.isEmpty(qmTokenInfo.getUserName())
                || expireTime <= 0){
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
            String token  = builder.sign(Algorithm.HMAC256(QmSercurityContent.getTokenSecret()));
            // AES加密手段
            token = QmSecurityAESUtil.encryptAES(token);
            return token;
        } catch (Exception e) {
            System.out.println("签名错误");
        }
        return null;
    }




    @Override
    public QmPermissions extractQmPermissions(final int roleId,final boolean isNew) {
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
                    QmSecurityRealm qmSecurityRealm = QmSecuritySpringApplication.getBean(QmSecurityRealm.class);
                    // 将该对象保存起来
                    List<String> matchUrls = qmSecurityRealm.authorizationPermissions(roleId);
                    // 替换该对象
                    qmPermissions.setMatchUrls(matchUrls);
                    QmPermissionsLis.set(i,qmPermissions);
                    // 将该集合缓存起来
                    application.setAttribute(QM_PERMISSIONS_KEY,QmPermissionsLis);
                }
                break;
            }
        }
        // 如果是空的则也使用调用者提供的方法去获取
        if (qmPermissions == null) {
            qmPermissions = new QmPermissions();
            List<String> matchUrls = QmSercurityContent.getQmSecurityRealm().authorizationPermissions(roleId);
            qmPermissions.setRoleId(roleId);
            qmPermissions.setMatchUrls(matchUrls);
            QmPermissionsLis.add(qmPermissions);
            application.setAttribute(QM_PERMISSIONS_KEY,QmPermissionsLis);
        }
        return qmPermissions;
    }

    @Override
    public List<QmPermissions> getAllQmPermissions() {
        List<QmPermissions> QmPermissionsLis = null;
        try {
            // 从ServletContext全局缓存中获取到该角色权限集合对象
            QmPermissionsLis = (List<QmPermissions>) application.getAttribute(QM_PERMISSIONS_KEY);
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

    @Override
    public QmTokenInfo getTokenInfo() {
        try {
            QmTokenInfo qmTokenInfo = (QmTokenInfo) request.getAttribute(QmTokenInfo.class.getName());
            return qmTokenInfo;
        } catch (Exception e) {
            return null;
        }
    }

}
