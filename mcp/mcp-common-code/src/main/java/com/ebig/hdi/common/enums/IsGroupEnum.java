package com.ebig.hdi.common.enums;

public enum IsGroupEnum {

	NO(0, "否"),
	YES(1, "是"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private IsGroupEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (IsGroupEnum c : IsGroupEnum.values()) {
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
