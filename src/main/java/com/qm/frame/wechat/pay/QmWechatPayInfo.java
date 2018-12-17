package com.qm.frame.wechat.pay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 微信支付信息实体类
 * 具体参数详细说明请移步到微信支付官方文档
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * @author 浅梦
 */
public final class QmWechatPayInfo {
	// 随机字符串，不长于32位。
	private String nonce_str = getRandomString(32);
	// 调用接口提交的终端设备号
	private String device_info;
	//商品描述交易字段格式根据不同的应用场景按照以下格式： APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
	private String body;
	// 商品详情
	private String detail;
	// 附加数据
	private String attach;
	// 商户订单号
	private String out_trade_no = createOut_trade_no();
	// 货币类型 默认CNY
	private String fee_type;
	// 订单总金额，单位为分
	private int total_fee;
	// 用户端实际ip
	private String spbill_create_ip;
	// 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
	private String time_start;
	// 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
	private String time_expire;
	// 订单优惠标记，代金券或立减优惠功能的参数
	private String goods_tag;
	// 支付类型 JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付
	private String trade_type;
	// trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
	private String product_id;
	// 上传此参数no_credit--可限制用户不能使用信用卡支付。指定支付方式
	private String limit_pay;
	// trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
	private String openid;
	// 场景信息 详见官网接口文档
	private String scene_info;

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScene_info() {
		return scene_info;
	}

	public void setScene_info(String scene_info) {
		this.scene_info = scene_info;
	}

	/**
	 * 生成随机订单号
	 * @return
	 */
	private String createOut_trade_no(){
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		return dataFormat.format(new Date()) + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
	}

	/**
	 * 生成随机字符串
	 * @param length
	 * @return
	 */
	private String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}

