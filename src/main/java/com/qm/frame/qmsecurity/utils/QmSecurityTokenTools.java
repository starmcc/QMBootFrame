package com.qm.frame.qmsecurity.utils;

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
     * @return
     * @throws Exception
     */
    public static String createToken(QmUserInfo qmUserInfo) throws Exception {
        Date date = new Date();
        StringBuffer stringBuffer = new StringBuffer();
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
        stringBuffer.append(QmAesEncDecTools.encrypt(identity, QmSecurityConstants.BASIC_KEY));
        stringBuffer.append("%@header@%");
        String user = QmSerializeTools.serializeToString(qmUserInfo.getUser());
        stringBuffer.append(QmAesEncDecTools.encrypt(user, QmSecurityConstants.BASIC_KEY));
        stringBuffer.append("%@body@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(tokenExp, QmSecurityConstants.BASIC_KEY));
        stringBuffer.append("%@tokenExp@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(matchExp, QmSecurityConstants.BASIC_KEY));
        stringBuffer.append("%@matchExp@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(signTime, QmSecurityConstants.BASIC_KEY));
        stringBuffer.append("%@signTime@%");
        String sign = QmSignTools.signMD5(stringBuffer.toString());
        stringBuffer.append(sign);
        LOG.info("※※※Token签发耗时：" + (System.currentTimeMillis() - date.getTime()) + "/ms※※※");
        return encryptWhile(stringBuffer.toString()).split("==")[0];
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
     * @return
     * @throws Exception
     */
    public static QmUserInfo analysisToken(String token) throws QmSecurityAnalysisTokenException {
        try {
            // 解密
            token = decryptWhile(token + "==");
            String tokenTemp = token;
            String[] temp = null;
            temp = tokenTemp.split("%@header@%");
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
            temp = tokenTemp.split("%@signTime@%");
            String signTime = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            // 组装对象
            QmUserInfo qmUserInfo = new QmUserInfo();
            qmUserInfo.setIdentify(identify);
            qmUserInfo.setUser(QmSerializeTools.deserializeToObject(userJson));
            long tokenExpTemp = 0;
            long matchUriExpTemp = 0;
            if (!QmSecurityConstants.TOKEN_EXP_INFINITE.equals(tokenExp)) {
                tokenExpTemp = Long.parseLong(tokenExp) / 1000;
            }
            if (!QmSecurityConstants.MATCHURI_EXP_INFINITE.equals(matchExp)) {
                matchUriExpTemp = Long.parseLong(matchExp) / 1000;
            }
            qmUserInfo.setTokenExpireTime(tokenExpTemp);
            qmUserInfo.setMatchUriExpireTime(matchUriExpTemp);
            qmUserInfo.setSignTime(new Date(Long.parseLong(signTime)));
            String sign = token.split("%@signTime@%")[1];
            String tokenReadSign = token.split("%@signTime@%")[0] + "%@signTime@%";
            if (verifySign(sign, tokenReadSign) == false) {
                throw new QmSecurityAnalysisTokenException();
            }
            return qmUserInfo;
        } catch (Exception e) {
            throw new QmSecurityAnalysisTokenException(e);
        }
    }

    /**
     * 校验token签名
     *
     * @param sign
     * @param token
     * @return
     */
    private static boolean verifySign(String sign, String token) {
        String signTemp = QmSignTools.signMD5(token);
        if (sign.trim().equals(signTemp)) {
            return true;
        }
        return false;
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
        String token = QmSecurityTokenTools.createToken(qmUserInfo);
        // 设置新旧token替换周期
        insertTokenContainer(qmUserInfo.getToken(), token, qmUserInfo.getIdentify());
        return token;
    }


    /**
     * 设置token到响应头
     *
     * @param response
     * @param token
     */
    public static void setResponseToken(HttpServletResponse response, String token) {
        // headers放行获取token的key
        String headerKey = "Access-Control-Expose-Headers";
        String typeName = "Content-Type," + QmSecurityContent.headerTokenKeyName;
        response.addHeader(headerKey, typeName);
        // 保存到响应头response的headers中
        response.setHeader(QmSecurityContent.headerTokenKeyName, token);
    }


    /**
     * token容器对象获取
     *
     * @param token
     * @param identify
     * @return
     */
    public static String tokenCo(String token, String identify) {
        TokenContainer tokenContainer = (TokenContainer)
                QmSecurityContent.qmSecurityCache.get(QmSecurityConstants.RESTART_SIGN_KEY + identify);
        if (tokenContainer != null && tokenContainer.getOldToken().equals(token)) {
            return tokenContainer.getNewToken();
        }
        return null;
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
        QmSecurityContent.qmSecurityCache.put(
                QmSecurityConstants.RESTART_SIGN_KEY + identify, tokenContainer, QmSecurityConstants.RESTART_SIGN_TIME);
    }


}
