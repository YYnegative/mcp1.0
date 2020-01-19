package com.ebig.hdi.common.matching.rules;

/**
 * 常量
 * @author zack
 *
 */
public interface MatchingConstants {
	
	/**
	 * 规则SQL
	 */
	String RULE_SQL = "select * from %s";
	/**
	 * 自动匹配类型
	 */
	int AUTO_RULE_TYPE = 0;
	/**
	 * 手动匹配类型
	 */
	int HANDLE_RULE_TYPE = 1;
}
