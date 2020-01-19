package com.ebig.hdi.common.enums;

public enum LabelTypeEnum {

	LABEL(0, "标签"),
	Batch (1, "批次码")
	;

	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private LabelTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (LabelTypeEnum c : LabelTypeEnum.values()) {
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
