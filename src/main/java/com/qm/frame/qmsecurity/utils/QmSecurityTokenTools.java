package com.qm.frame.qmsecurity.utils;

import com.alibaba.fastjson.JSON;
import com.qm.frame.qmsecurity.config.QmSecurityConstants;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.entity.TokenContainer;
import com.qm.frame.qmsecurity.exception.QmSecurityAnalysisTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 对称加密技术
 *
 * @author 浅梦
 */
public class QmSecurityTokenTools {

    /**
     * logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(QmSecurityTokenTools.class);

    /**
     * 加密封装
     *
     * @param data
     * @return
     * @throws Exception
     */
    private static String encryptWhile(String data) throws Exception {
        String str = data;
        for (int i = 0; i < QmSecurityContent.encryptNumber; i++) {
            str = QmAesEncDecTools.encrypt(str, QmSecurityContent.tokenSecret);
        }
        return str;
    }

    /**
     * 解密封装
     *
     * @param data
     * @return
     * @throws Exception
     */
    private static String decryptWhile(String data) throws Exception {
        String str = data;
        for (int i = 0; i < QmSecurityContent.encryptNumber; i++) {
            str = QmAesEncDecTools.decrypt(str, QmSecurityContent.tokenSecret);
        }
        return str;
    }


    /**
     * token签发方法
     *
     * @param qmUserInfo
     * @return token
     * @throws Exception
     */
    public static String createToken(QmUserInfo qmUserInfo) throws Exception {
        Date date = new Date();
        String identity = qmUserInfo.getIdentify();
        String tokenExp = QmSecurityConstants.TOKEN_EXP_INFINITE;
        String matchExp = QmSecurityConstants.MATCHURI_EXP_INFINITE;
        if (qmUserInfo.getTokenExpireTime() != 0) {
            tokenExp = String.valueOf(qmUserInfo.getTokenExpireTime() * 1000);
        }
        if (qmUserInfo.getMatchUriExpireTime() != 0) {
            matchExp = String.valueOf(qmUserInfo.getMatchUriExpireTime() * 1000);
        }
        String signTime = String.valueOf(qmUserInfo.getSignTime().getTime());
        String singleSignOn = String.valueOf(String.valueOf(qmUserInfo.isSingleSignOn()));
        // 开始签名
        StringBuffer token = new StringBuffer();
        token.append(QmAesEncDecTools.encrypt(identity, QmSecurityConstants.BASIC_KEY));
        token.append("%@header@%");
        String user = JSON.toJSONString(qmUserInfo.getUser());
        token.append(QmAesEncDecTools.encrypt(user, QmSecurityConstants.BASIC_KEY));
        token.append("%@body@%");
        token.append(QmAesEncDecTools.encrypt(tokenExp, QmSecurityConstants.BASIC_KEY));
        token.append("%@tokenExp@%");
        token.append(QmAesEncDecTools.encrypt(matchExp, QmSecurityConstants.BASIC_KEY));
        token.append("%@matchExp@%");
        token.append(QmAesEncDecTools.encrypt(singleSignOn, QmSecurityConstants.BASIC_KEY));
        token.append("%@singleSignOn@%");
        token.append(QmAesEncDecTools.encrypt(signTime, QmSecurityConstants.BASIC_KEY));
        token.append("%@signTime@%");
        StringBuffer signBf = new StringBuffer();
        signBf.append(identity);
        signBf.append(user);
        signBf.append(tokenExp);
        signBf.append(matchExp);
        signBf.append(singleSignOn);
        signBf.append(signTime);
        String sign = QmSecurityTokenTools.signUserInfo(identity, user, tokenExp, matchExp, singleSignOn, signTime);
        token.append(sign);
        LOG.info("※※※Token签发耗时：" + (System.currentTimeMillis() - date.getTime()) + "/ms※※※");
        return encryptWhile(token.toString()).split("=")[0];
    }


    /**
     * token时效校验
     *
     * @param exp
     * @param signTime
     * @return
     */
    public static boolean verifyExp(long exp, long signTime) {
        // 校验token是否失效
        if (exp != 0) {
            Date tokenExp = new Date(signTime + (exp * 1000));
            if (tokenExp.getTime() <= System.currentTimeMillis()) {
                // 失效了。
                return false;
            }
        }
        return true;
    }


    /**
     * 解析token
     *
     * @param token
     * @return identify 用户唯一识别
     * @throws Exception
     */
    public static String analysisToken(String token) throws QmSecurityAnalysisTokenException {
        try {
            // 解密
            token = decryptWhile(token + "=");
            String tokenTemp = token;
            String[] temp = tokenTemp.split("%@header@%");
            String identify = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@body@%");
            String userJson = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@tokenExp@%");
            String tokenExp = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@matchExp@%");
            String matchExp = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@singleSignOn@%");
            String singleSignOn = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@signTime@%");
            String signTime = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            String sign = temp[1];
            // 解析无限时间
            String signTemp = QmSecurityTokenTools.signUserInfo(
                    identify, userJson, tokenExp, matchExp, singleSignOn, signTime);
            if (!sign.equals(signTemp)) {
                throw new QmSecurityAnalysisTokenException();
            }
            return identify;
        } catch (Exception e) {
            throw new QmSecurityAnalysisTokenException(e);
        }
    }

    /**
     * 签名
     *
     * @param identity
     * @param user
     * @param tokenExp
     * @param matchExp
     * @param singleSignOn
     * @param signTime
     * @return
     */
    private static String signUserInfo(String identity,
                                       String user,
                                       String tokenExp,
                                       String matchExp,
                                       String singleSignOn,
                                       String signTime) {
        StringBuffer signBf = new StringBuffer();
        signBf.append(identity);
        signBf.append(user);
        signBf.append(tokenExp);
        signBf.append(matchExp);
        signBf.append(singleSignOn);
        signBf.append(signTime);
        return QmSignTools.signMD5(signBf.toString());
    }

    /**
     * 重新签发token
     *
     * @param token
     * @return
     */
    public static String restartAuth(QmUserInfo qmUserInfo) throws Exception {
        // 先删除该缓存的token值
        QmSecurityContent.qmSecurityCache.remove(
                QmSecurityConstants.USER_KEY + qmUserInfo.getIdentify());
        // 进行重新签发token
        return QmSecurityTokenTools.createToken(qmUserInfo);
    }


    /**
     * 设置token到响应头
     *
     * @param response
     * @param token
     */
    public static void sendResponseToken(HttpServletResponse response, String token) {
        // headers放行获取token的key
        String headerKey = "Access-Control-Expose-Headers";
        String typeName = "Content-Type," + QmSecurityContent.headerTokenKeyName;
        response.addHeader(headerKey, typeName);
        // 保存到响应头response的headers中
        response.setHeader(QmSecurityContent.headerTokenKeyName, token);
        LOG.info("设置了Response " + QmSecurityContent.headerTokenKeyName + "响应头");
    }


    /**
     * token容器对象获取
     *
     * @param identify
     * @return
     */
    public static TokenContainer getTokenContainer(String identify) {
        TokenContainer tokenContainer = (TokenContainer)
                QmSecurityContent.qmSecurityCache.get(QmSecurityConstants.RESTART_SIGN_KEY + identify);
        return tokenContainer;
    }

    /**
     * 插入token交替容器
     *
     * @param oldToken
     * @param newToken
     * @param identify
     */
    public static void insertTokenContainer(String oldToken, String newToken, String identify) {
        // 设置新旧token替换周期
        TokenContainer tokenContainer = new TokenContainer();
        tokenContainer.setNewToken(newToken);
        tokenContainer.setOldToken(oldToken);
        QmSecurityContent.qmSecurityCache.put(QmSecurityConstants.RESTART_SIGN_KEY + identify,
                tokenContainer, QmSecurityConstants.RESTART_SIGN_TIME);
    }


}
