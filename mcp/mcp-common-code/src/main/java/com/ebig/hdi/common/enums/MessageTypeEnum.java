package com.ebig.hdi.common.enums;

public enum MessageTypeEnum {

	SYSTEM_NOTIFICATION(0, "系统通知"),
	CERTIFICATE_WARN(1, "证件提醒"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private MessageTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (MessageTypeEnum c : MessageTypeEnum.values()) {
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
