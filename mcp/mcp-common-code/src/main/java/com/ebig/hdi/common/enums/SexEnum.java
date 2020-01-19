package com.ebig.hdi.common.enums;
/**
 * 类功能说明：性别枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2019年06月11日 上午09:41:32 <br/>
 * 版本：V1.0 <br/>
 */
public enum SexEnum {

	MAN(1, "男"),
	WOMAN(2, "女"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private SexEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (SexEnum c : SexEnum.values()) {
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
