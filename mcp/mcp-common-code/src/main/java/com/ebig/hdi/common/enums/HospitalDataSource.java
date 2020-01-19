package com.ebig.hdi.common.enums;

public enum HospitalDataSource {

	SYSTEM(0, "系统录入"),
	SPD(1, "医院SPD"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private HospitalDataSource(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (HospitalDataSource c : HospitalDataSource.values()) {
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
