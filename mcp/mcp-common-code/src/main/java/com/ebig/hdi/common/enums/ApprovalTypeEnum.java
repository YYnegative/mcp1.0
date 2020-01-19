package com.ebig.hdi.common.enums;

/**
 * 审批状态
 * @author clang
 *
 */
public enum ApprovalTypeEnum {
	

	WAIT(0,"待审批"),
	PASS(1, "审批通过"),
	FAIL(2,"审批不通过"),
	;
	
	/// 成员变量
	private Integer key;
	private String value;

	// 构造方法
	ApprovalTypeEnum(Integer key, String value) {
			this.key = key;
			this.value = value;
		}

	// 普通方法
	public static String getName(Integer key) {
		for (ApprovalTypeEnum c : ApprovalTypeEnum.values()) {
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
