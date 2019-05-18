package com.qm.frame.qmsecurity.utils;

import com.alibaba.fastjson.JSON;
import com.qm.frame.qmsecurity.config.QmSecurityContent;
import com.qm.frame.qmsecurity.entity.QmUserInfo;
import com.qm.frame.qmsecurity.exception.QmSecurityAnalysisTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * 底层加解密key
     */
    private static final String BASIC_KEY = "hfsdnvfjdmfkl";

    /**
     * token无限期伪装字符串
     */
    private static final String TOKEN_EXP_INFINITE = "shdhwaxnlxhueicn";

    /**
     * 权限缓存无限时间伪装字符串
     */
    private static final String MATCHURI_EXP_INFINITE = "asdhiuesnd31270ccb";

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
        String userJson = JSON.toJSONString(qmUserInfo.getUser());
        String tokenExp = TOKEN_EXP_INFINITE;
        String matchExp = MATCHURI_EXP_INFINITE;
        if (qmUserInfo.getTokenExpireTime() != 0) {
            tokenExp = String.valueOf(qmUserInfo.getTokenExpireTime() * 1000);
        }
        if (qmUserInfo.getMatchUriExpireTime() != 0) {
            matchExp = String.valueOf(qmUserInfo.getMatchUriExpireTime() * 1000);
        }
        qmUserInfo.setSignTime(date);
        String signTime = String.valueOf(date.getTime());
        stringBuffer.append(QmAesEncDecTools.encrypt(identity, BASIC_KEY));
        stringBuffer.append("%@header@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(userJson, BASIC_KEY));
        stringBuffer.append("%@body@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(qmUserInfo.getUser().getClass().getTypeName(), BASIC_KEY));
        stringBuffer.append("%@className@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(tokenExp, BASIC_KEY));
        stringBuffer.append("%@tokenExp@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(matchExp, BASIC_KEY));
        stringBuffer.append("%@matchExp@%");
        stringBuffer.append(QmAesEncDecTools.encrypt(signTime, BASIC_KEY));
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
            String identify = QmAesEncDecTools.decrypt(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@body@%");
            String userJson = QmAesEncDecTools.decrypt(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@className@%");
            String className = QmAesEncDecTools.decrypt(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@tokenExp@%");
            String tokenExp = QmAesEncDecTools.decrypt(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@matchExp@%");
            String matchExp = QmAesEncDecTools.decrypt(temp[0], BASIC_KEY);
            tokenTemp = temp[1];
            temp = tokenTemp.split("%@signTime@%");
            String signTime = QmAesEncDecTools.decrypt(temp[0], BASIC_KEY);
            // 组装对象
            QmUserInfo qmUserInfo = new QmUserInfo();
            qmUserInfo.setIdentify(identify);
            qmUserInfo.setUser(JSON.parseObject(userJson, Class.forName(className)));
            long tokenExpTemp = 0;
            long matchUriExpTemp = 0;
            if (!TOKEN_EXP_INFINITE.equals(tokenExp)) {
                tokenExpTemp = Long.parseLong(tokenExp) / 1000;
            }
            if (!MATCHURI_EXP_INFINITE.equals(matchExp)) {
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

}
