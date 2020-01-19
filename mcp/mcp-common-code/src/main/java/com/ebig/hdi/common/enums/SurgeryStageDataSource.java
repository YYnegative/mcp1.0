package com.ebig.hdi.common.enums;

public enum SurgeryStageDataSource {

	SURGERY(1, "手术单"),
	STAGE_ADD(2, "跟台目录添加"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private SurgeryStageDataSource(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (SurgeryStageDataSource c : SurgeryStageDataSource.values()) {
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
