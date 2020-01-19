package com.ebig.hdi.common.enums;

/**
 * @title: ActTypeEnum
 * @projectName mcp
 * @description: 审批类型枚举
 * @author：wenchao
 * @date：2019-11-22 10:33
 * @version：V1.0
 */
public enum ActTypeEnum{
    FACTORY(0,"厂商"),
	PLATFORM(1, "平台"),
	HOSPITAL(2, "医院"),
	SUPPLY(3, "配送商(供应商)"),
	AGENT(4, "全国代理"),
	DRUGSTORE(5, "药店"),
	PLATFORM_CONSUMABLES(6,"平台耗材"),
	PLATFORM_DRUGS(7, "平台药品"),
	PLATFORM_REAGENT(8, "平台试剂"),
	HOSPITAL_PLATFORM_CONSUMABLES_MATCH(9,"医院平台耗材匹配"),
	HOSPITAL_PLATFORM_DRUGS_MATCH(10,"医院平台药品匹配"),
	HOSPITAL_PLATFORM_REAGENT_MATCH(11, "医院平台试剂匹配"),
	SUPPLIER_PLATFORM_CONSUMABLES_MATCH(12,"供应商平台耗材匹配"),
	SUPPLIER_PLATFORM_DRUGS_MATCH(13,"供应商平台药品匹配"),
	SUPPLIER_PLATFORM_REAGENT_MATCH(14, "供应商平台试剂匹配"),
  ;

	private Integer key;
	private String value;

    /**
     * 构造方法(私有)
     * @param key
     * @param value
     */
	 ActTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (ActTypeEnum c : ActTypeEnum.values()) {
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
