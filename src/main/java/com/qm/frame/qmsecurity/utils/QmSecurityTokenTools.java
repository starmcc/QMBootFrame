package com.qm.frame.qmsecurity.utils;

import com.qm.frame.qmsecurity.config.QmSecurityConstants;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityAnalysisTokenException;
import com.qm.frame.qmsecurity.exception.QmSecurityCreateTokenException;
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
        for (int i = 0; i < QmSecurityContent.getEncryptNumber(); i++) {
            str = QmAesEncDecTools.encrypt(str, QmSecurityContent.getTokenSecret());
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
        for (int i = 0; i < QmSecurityContent.getEncryptNumber(); i++) {
            str = QmAesEncDecTools.decrypt(str, QmSecurityContent.getTokenSecret());
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
    public static String createToken(QmUserInfo qmUserInfo) throws QmSecurityCreateTokenException {
        try {
            Date date = new Date();
            String identity = qmUserInfo.getIdentify();
            String tokenExp = String.valueOf(qmUserInfo.getTokenExpireTime() * 1000);
            String signTime = String.valueOf(qmUserInfo.getSignTime().getTime());
            // 开始签名
            StringBuffer token = new StringBuffer();
            token.append(QmAesEncDecTools.encrypt(identity, QmSecurityConstants.BASIC_KEY));
            token.append("%@header@%");
            token.append(QmAesEncDecTools.encrypt(qmUserInfo.getUser(), QmSecurityConstants.BASIC_KEY));
            token.append("%@body@%");
            token.append(QmAesEncDecTools.encrypt(String.valueOf(qmUserInfo.getTokenExpireTime()), QmSecurityConstants.BASIC_KEY));
            token.append("%@tokenExp@%");
            token.append(QmAesEncDecTools.encrypt(signTime, QmSecurityConstants.BASIC_KEY));
            token.append("%@signTime@%");
            StringBuffer signBf = new StringBuffer();
            signBf.append(identity);
            signBf.append(qmUserInfo.getUser());
            signBf.append(tokenExp);
            signBf.append(signTime);
            String sign = QmSecurityTokenTools.signUserInfo(qmUserInfo);
            token.append(sign);
            LOG.info("※※※Token签发耗时：" + (System.currentTimeMillis() - date.getTime()) + "/ms※※※");
            return encryptWhile(token.toString()).split("=")[0];
        } catch (Exception e) {
            throw new QmSecurityCreateTokenException(e);
        }
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
    public static QmUserInfo analysisToken(String token) throws QmSecurityAnalysisTokenException {
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
            temp = tokenTemp.split("%@signTime@%");
            String signTime = QmAesEncDecTools.decrypt(temp[0], QmSecurityConstants.BASIC_KEY);
            String sign = temp[1];
            // 封装对象
            QmUserInfo qmUserInfo = new QmUserInfo();
            qmUserInfo.setIdentify(identify);
            qmUserInfo.setUser(userJson);
            qmUserInfo.setTokenExpireTime(Long.valueOf(tokenExp));
            qmUserInfo.setSignTime(new Date(Long.valueOf(signTime)));
            // 还原签名
            String signTemp = QmSecurityTokenTools.signUserInfo(qmUserInfo);
            // 校验签名
            if (!sign.equals(signTemp)) {
                throw new QmSecurityAnalysisTokenException();
            }
            return qmUserInfo;
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
     * @param signTime
     * @return
     */
    private static String signUserInfo(QmUserInfo qmUserInfo) {
        StringBuffer signBf = new StringBuffer();
        signBf.append(qmUserInfo.getIdentify());
        signBf.append(qmUserInfo.getUser());
        signBf.append(qmUserInfo.getTokenExpireTime());
        signBf.append(qmUserInfo.getSignTime().getTime());
        return QmSignTools.signMD5(signBf.toString());
    }

    /**
     * 设置token到响应头
     *
     * @param response
     * @param token
     */
    private static void sendResponseToken(HttpServletResponse response, String token) {
        // headers放行获取token的key
        String headerKey = "Access-Control-Expose-Headers";
        String typeName = "Content-Type," + QmSecurityContent.getHeaderTokenKeyName();
        response.addHeader(headerKey, typeName);
        // 保存到响应头response的headers中
        response.setHeader(QmSecurityContent.getHeaderTokenKeyName(), token);
        LOG.info("设置了Response " + QmSecurityContent.getHeaderTokenKeyName() + "响应头!");
    }

    /**
     * 重新签发token
     *
     * @param qmUserInfo
     * @param response
     * @throws QmSecurityCreateTokenException
     */
    public static void restartCreateToken(QmUserInfo qmUserInfo, HttpServletResponse response) throws QmSecurityCreateTokenException {
        qmUserInfo.setSignTime(new Date());
        QmSecurityTokenTools.sendResponseToken(response, QmSecurityTokenTools.createToken(qmUserInfo));
    }


}
