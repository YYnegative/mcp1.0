package com.ebig.hdi.common.enums;
/**
 * 类功能说明：类型枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2019年05月17日 下午4:05:38 <br/>
 * 版本：V1.0 <br/>
 */
public enum TypeEnum {

	USER_PLATFORM(0, "平台用户"),
	USER_SUPPLIER(1, "供应商用户"),
	HOSPITAL_SUPPLIER(2, "医院用户")
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private TypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (TypeEnum c : TypeEnum.values()) {
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
