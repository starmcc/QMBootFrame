package com.qm.frame.wechat.pay;

import java.util.Map;

/**
 * Copyright © 2018浅梦工作室}. All rights reserved.
 *
 * @author 浅梦
 * @date 2018/12/17 1:40
 * @Description: TODO
 */
public interface QmWechatCallBack {

    /**
     * 支付回调函数
     * @param payResultInfo 支付回调信息
     */
    void payResultInfo(Map<String, String> payResultInfo);
}
