package com.ebig.hdi.common.enums;

public enum LicenseTypeEnum {

	LICENSE_GOODS(0, "商品证照"),
	LICENSE_SUPPLIER(1, "供应商证照"),
	LICENSE_FACTORY(2, "厂商证照"),
	LICENSE_AGENT(3, "代理商证照")
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private LicenseTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (LicenseTypeEnum c : LicenseTypeEnum.values()) {
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