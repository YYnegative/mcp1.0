package com.ebig.hdi.common.enums;

public enum IsMatchEnum {

	NO(0, "未匹对"),
	YES(1, "已匹对"),
	CANCEL(2, "取消匹对"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private IsMatchEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (IsMatchEnum c : IsMatchEnum.values()) {
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
