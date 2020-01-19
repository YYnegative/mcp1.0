package com.ebig.hdi.common.matching.rules;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;

import lombok.Data;

/**
 * 匹配规则项内容
 * 
 * @author zack
 *
 */
@Data
@ObjectCreate(pattern = "matching-rules/rules/rule")
public class Rule {

	/**
	 * 原匹配键值
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/rule/key")
	private String key;

	/**
	 * 需要匹配的目标列名,如果表有别名时，为别名.列名
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/rule/destcolumn")
	private String destColumn;

	/**
	 * 相似度
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/rule/similarity")
	private double similarity;

	/**
	 * 展示数量
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/rule/showqty")
	private int showQty;
}
