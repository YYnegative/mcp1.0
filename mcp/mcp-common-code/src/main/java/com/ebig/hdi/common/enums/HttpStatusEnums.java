package com.ebig.hdi.common.enums;

/**
 * @description:mcp响应状态码
 * @author: wenchao
 * @time: 2019-10-24 14:29
 */
public enum HttpStatusEnums {
	ERRCODE_OK(0, "没有错误返回"),
	ERRCODE_ILLEGAL_XML(1, "不合法xml文件"),
	ERRCODE_ILLEGAL_DATA(2, "不合法数据"),
	ERRCODE_INVOKE_GRANT(3, "调用未授权"),
	ERRCODE_INVOKE_NOSERVICE(4, "无此服务"),
	ERRCODE_INVOKE_ERR(5, "调用服务错误"),
	ERRCODE_LOGIN_ERR(6, "登录错误"),
	ERRCODE_UNKNOWN(100, "未知错误"),
	;

	// 成员变量
	private Integer key;
	private String value;

	/**
	 * 构造方法
	 * @param key
	 * @param value
     */
	 HttpStatusEnums(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	// 普通方法
	public static String getName(Integer key) {
		for (HttpStatusEnums c : HttpStatusEnums.values()) {
			if (c.getKey().equals(key)) {
				return c.value;
			}
		}
		return null;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
