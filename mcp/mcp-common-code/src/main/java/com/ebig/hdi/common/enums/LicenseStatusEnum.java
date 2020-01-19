package com.ebig.hdi.common.enums;
/**
 * 类功能说明：证照状态枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2017年10月24日 下午4:51:57 <br/>
 * 版本：V1.0 <br/>
 */
public enum LicenseStatusEnum {

	NORMAL(0, "正常"),
	WARNING(1, "预警"),
	INVALID(2, "失效"),
	REPLACING(3, "换证中")
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private LicenseStatusEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (LicenseStatusEnum c : LicenseStatusEnum.values()) {
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
