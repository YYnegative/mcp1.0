package com.ebig.hdi.common.enums;
/**
 * 类功能说明：通用状态枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2017年10月24日 下午4:51:57 <br/>
 * 版本：V1.0 <br/>
 */
public enum StatusEnum {

	DISABLE(0, "停用"),
	USABLE(1, "启用"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private StatusEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (StatusEnum c : StatusEnum.values()) {
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
