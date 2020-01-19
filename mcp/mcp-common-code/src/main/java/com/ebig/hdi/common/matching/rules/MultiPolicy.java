package com.ebig.hdi.common.matching.rules;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

import lombok.Data;
/**
 * 匹配多条结果集返回策略
 * @author zack
 *
 */
@Data
@ObjectCreate(pattern = "matching-rules/rules/multipolicy")
public class MultiPolicy {

	/**
	 * 策略名 none,first,last,all
	 */
	@SetProperty(attributeName = "type", pattern = "matching-rules/rules/multipolicy")
	private String type;
	
	/**
	 * 策略列名
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/multipolicy/policycolumn")
	private String policyColumn;
	
	/**
	 * 生效索引，当ensort为true时生效,从1开始
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/multipolicy/enindex")
	private int enIndex;
}
