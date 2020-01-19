package com.ebig.hdi.common.enums;

public enum DataSourceEnum {

	MANUAL(1, "手动录入"),
	PORT(2, "接口录入")
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private DataSourceEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (DataSourceEnum c : DataSourceEnum.values()) {
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
