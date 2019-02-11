package com.qm.frame.basic.controller;

/**
 * @author 状态码工具类
 * @createDate 2018年10月26日 上午12:58:22
 * @Description 数据请求、返回工具类
 */
public enum QmCode {
	/**
	 * 成功
	 */
	_1(1),
	/**
	 * 失败
	 */
	_2(2),
	/**
	 * 自定义
	 */
	_3(3),
	/**
	 * 自定义
	 */
	_4(4),
	/**
	 * 自定义
	 */
	_5(5),
	/**
	 * 自定义
	 */
	_6(6),
	/**
	 * 自定义
	 */
	_7(7),
	/**
	 * 自定义
	 */
	_8(8),
	/**
	 * 自定义
	 */
	_9(9),
	/**
	 * 自定义
	 */
	_10(10),
	/**
	 * 参数提供不完整
	 */
	_100(100),
	/**
	 * 参数错误
	 */
	_101(101),
	/**
	 * 版本号未通过
	 */
	_102(102),
	/**
	 * 未登录/登录过期/请求ip校验失败
	 */
	_103(103),
	/**
	 * 权限不足，拒绝访问
	 */
	_104(104),
	/**
	 * 请求404,找不到资源
	 */
	_404(404),
	/**
	 * 请求405,请求类型错误POST or GET
	 */
	_405(405),
	/**
	 * 请求415,不支持的媒体类型
	 */
	_415(415),
	/**
	 * 服务器错误
	 */
	_500(500),
	/**
	 * 未知错误
	 */
	_999(999);

	private int code;

	private QmCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	/**
	 * 根据code获取对应msg信息
	 * @param code
	 * @return msg
	 */
	public static final String getMsg(QmCode code) {
		String msg;
		switch (code) {
		case _1:
			msg = "Success";
			break;
		case _2:
			msg = "Defeated";
			break;
		case _100:
			msg = "Parameter is incomplete";
			break;
		case _101:
			msg = "parameter error";
			break;
		case _102:
			msg = "Version validation failed";
			break;
		case _103:
			msg = "Login not in";
			break;
		case _104:
			msg = "Permission denied";
			break;
		case _404:
			msg = "Can't find resources";
			break;
		case _405:
			msg = "Wrong request mode";
			break;
		case _415:
			msg = "Unsupported media types";
			break;
		case _500:
			msg = "Server Error";
			break;
		case _999:
			msg = "Server unknow";
			break;
		default:
			msg = "Unknown Message";
			break;
		}
		return msg;
	}
}
