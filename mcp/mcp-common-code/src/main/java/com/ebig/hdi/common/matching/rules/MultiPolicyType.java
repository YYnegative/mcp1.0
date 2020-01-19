package com.ebig.hdi.common.matching.rules;

import lombok.Getter;

/**
 * 策略类型枚举
 * 
 * @author zack
 *
 */
public enum MultiPolicyType {

	NONE("none"), FIRST("first"), LAST("last"), ALL("all");

	@Getter
	private String type;

	private MultiPolicyType(String type) {
		this.type = type;
	}
}
