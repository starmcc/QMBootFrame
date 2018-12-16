package com.qm.frame.wechat.pay;

import com.qm.frame.wechat.QmWechat;
import org.jdom.JDOMException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 微信支付实例
 * @author 浅梦
 */
public final class QmWechatPay implements QmWechat {

	/**
	 * 微信支付-统一下单
	 * @param qmWechatPayInfo 微信支付信息实体类
	 * @param apiKey 微信【商户】秘钥,该值请在微信公众号关联的商户号中进行获取
	 * @param requestUrl 微信统一下单接口地址
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws JDOMException
	 */
	public Map<String,String> pay(QmWechatPayInfo qmWechatPayInfo, String apiKey, String requestUrl)
			throws IllegalArgumentException,
			IllegalAccessException,
			IOException,
			JDOMException{
		// 签名
		String sign = QmWechatPayBasis.createSign("UTF-8",qmWechatPayInfo,apiKey);
		qmWechatPayInfo.setSign(sign);
		//将微信信息对象置换为XML报文形式
		String requestXML = QmWechatPayBasis.getRequestXml(qmWechatPayInfo);
		System.out.println("请求报文：\n" + requestXML);
		String resultXML = QmWechatPayBasis.httpsRequest(requestUrl, "POST", requestXML);
		System.out.println("返回报文：\n" + resultXML);
		Map<String,String> resMap = QmWechatPayBasis.doXMLParse(resultXML);
		System.out.println("返回具体信息：\n" + resMap.toString());
		return resMap;
	}

	/**
	 * 微信异步回调通知
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param qmWechatCallBack 回调函数 使用new QmWechatCallBack 进行参数列表回调方法调用。
	 * @throws IOException
	 * @throws JDOMException
	 */
	public void payReadit(HttpServletRequest request,
						  HttpServletResponse response,
						  QmWechatCallBack qmWechatCallBack)
			throws IOException, JDOMException{
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		String resultStr  = new String(outSteam.toByteArray(),"utf-8");
		System.out.println("支付成功的回调："+resultStr);
		Map<String, String> resultInfo = QmWechatPayBasis.doXMLParse(resultStr);
		// 调用回调函数
		qmWechatCallBack.payResultInfo(resultInfo);
		// 通知微信服务器已接收到信息
		StringBuffer resXml = new StringBuffer();
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		resXml.append("<xml>");
		resXml.append("<return_code><![CDATA[SUCCESS]]></return_code>");
		resXml.append("<return_msg><![CDATA[OK]]></return_msg>");
		resXml.append("</xml>");
		out.write(resXml.toString().getBytes());
	}

}

