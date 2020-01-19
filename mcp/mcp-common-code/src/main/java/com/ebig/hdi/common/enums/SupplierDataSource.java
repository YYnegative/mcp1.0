package com.ebig.hdi.common.enums;

public enum SupplierDataSource {

	SYSTEM(0, "系统录入"),
	ERP(1, "供应商ERP"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private SupplierDataSource(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (SupplierDataSource c : SupplierDataSource.values()) {
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
