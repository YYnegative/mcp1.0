package com.ebig.hdi.common.enums;

public enum DelFlagEnum {

	DELETE(-1, "已删除"),
	NORMAL(0, "正常"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private DelFlagEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (DelFlagEnum c : DelFlagEnum.values()) {
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
