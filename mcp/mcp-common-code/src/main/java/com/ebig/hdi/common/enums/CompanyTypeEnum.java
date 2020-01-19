package com.ebig.hdi.common.enums;
/**
 * 类功能说明：spd机构类型枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2019年06月18日 下午14:36:32 <br/>
 * 版本：V1.0 <br/>
 */
public enum CompanyTypeEnum {

	FACTORY(0,"厂商"),
	PLATFORM(1, "平台"),
	HOSPITAL(2, "医院"),
	SUPPLY(3, "配送商(供应商)"),
	AGENT(4, "全国代理"),
	DRUGSTORE(5, "药店"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private CompanyTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (CompanyTypeEnum c : CompanyTypeEnum.values()) {
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
