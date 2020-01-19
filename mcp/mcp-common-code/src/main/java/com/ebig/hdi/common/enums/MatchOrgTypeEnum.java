package com.ebig.hdi.common.enums;

public enum MatchOrgTypeEnum {

	HOSPITAL(0, "医院"),
	SUPPLIER(1, "供应商")
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private MatchOrgTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (MatchOrgTypeEnum c : MatchOrgTypeEnum.values()) {
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
