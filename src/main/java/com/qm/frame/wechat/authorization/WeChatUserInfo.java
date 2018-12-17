package com.qm.frame.wechat.authorization;

/**
 * @author 浅梦工作室
 * @createDate 2018年9月20日 上午12:34:19
 * @Description 微信用户信息实体类
 */
public class WeChatUserInfo {
	private String openId; // 用户的唯一标识
	private String nickName; // 用户昵称
	private String token; // token
	private Integer sex; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String province; // 用户个人资料填写的省份
	private String city;// 普通用户个人资料填写的城市
	private String country;// 国家，如中国为CN
	private String headImgUrl;// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	private String privilege;// 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	private String unionid;// 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。


	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public String getOpenId() {
		return openId;
	}

	public String getToken() {
		return token;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setToken(String token) {
		this.token = token;
	}

}