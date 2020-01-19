package com.ebig.hdi.common.enums;

public enum MessageStatusEnum {

	DRAFT(0, "草稿"),
	PUBLISH(1, "已发布"),
	DELETE(2, "已删除"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private MessageStatusEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (MessageStatusEnum c : MessageStatusEnum.values()) {
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
