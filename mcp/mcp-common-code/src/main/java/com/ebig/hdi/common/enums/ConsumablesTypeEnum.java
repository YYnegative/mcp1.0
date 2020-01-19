package com.ebig.hdi.common.enums;

public enum ConsumablesTypeEnum {

	ORTHOPEDICS_CONSUMABLES("0", "骨科耗材"), 
	;

	// 成员变量
	private String key;
	private String value;

	// 构造方法
	 ConsumablesTypeEnum(String key, String value) {
				this.key = key;
				this.value = value;
			}

	// 普通方法
	public static String getName(String key) {
		for (ConsumablesTypeEnum c : ConsumablesTypeEnum.values()) {
			if (c.getKey().equals(key)) {
				return c.value;
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
