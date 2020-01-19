package com.ebig.hdi.common.enums;

public enum MatchGoodsTypeEnum {

	DRUGS(1, "药品"),
	REAGENT(2, "试剂"),
	CONSUMABLE(3, "耗材")
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private MatchGoodsTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (MatchGoodsTypeEnum c : MatchGoodsTypeEnum.values()) {
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
