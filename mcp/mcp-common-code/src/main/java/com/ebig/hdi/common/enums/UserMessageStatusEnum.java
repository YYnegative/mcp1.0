package com.ebig.hdi.common.enums;

public enum UserMessageStatusEnum {

	UNREAD(0, "未读"),
	READ(1, "已读"),
	DELETE(2, "删除"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private UserMessageStatusEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (UserMessageStatusEnum c : UserMessageStatusEnum.values()) {
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
