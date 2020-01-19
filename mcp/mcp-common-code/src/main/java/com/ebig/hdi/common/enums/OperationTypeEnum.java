package com.ebig.hdi.common.enums;
/**
 * 操作类型枚举类
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-07-23 04:19:11
 */
public enum OperationTypeEnum {

	APPROVAL(0, "审批"),
	MATCH(1, "匹对"),
	GOODS_INFORMATION_MODIFY(2, "商品信息变更"),
	CANCEL_MATCH(3, "取消匹对")
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private OperationTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (OperationTypeEnum c : OperationTypeEnum.values()) {
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
