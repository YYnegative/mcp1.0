package com.ebig.hdi.common.enums;
/**
 * 类功能说明：退货申请类型枚举类<br/>
 * 类修改者：<br/>
 * 修改日期：<br/>
 * 修改说明：<br/>
 * 修改说明：<br/>
 * 创建时间：2019年05月29日 上午11:50:08 <br/>
 * 版本：V1.0 <br/>
 */
public enum RefundsApplyTypeEnum {

	UNCONFIRMED(0, "未确认"),
	EXPIRED(1, "已作废"),
	CONFIRMED(2, "已确认"),
	RETURNED(3, "已退货"),
	PART_RETURNED(4, " 部分退货"),
	;
	
	// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	private RefundsApplyTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (RefundsApplyTypeEnum c : RefundsApplyTypeEnum.values()) {
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
