package com.ebig.hdi.common.enums;

public enum GoodsNatureEnum {

	//国产
	DOMESTIC(0, "A"),
	//进口
	IMPORTED(1, "B"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private GoodsNatureEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (GoodsNatureEnum c : GoodsNatureEnum.values()) {
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
