package com.ebig.hdi.common.enums;

public enum FactoryStatusEnum {

	DRAFT(-1, "待审批"),
	DISABLE(0, "停用"),
	USABLE(1, "启用"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private FactoryStatusEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (FactoryStatusEnum c : FactoryStatusEnum.values()) {
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
