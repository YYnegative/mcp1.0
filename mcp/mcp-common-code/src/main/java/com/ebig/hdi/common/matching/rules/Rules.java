package com.ebig.hdi.common.matching.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;

import lombok.Data;

/**
 * 匹配规则项
 * 
 * @author zack
 *
 */
@Data
@ObjectCreate(pattern = "matching-rules/rules")
public class Rules {

	/**
	 * 规则名称
	 */
	@SetProperty(attributeName = "name", pattern = "matching-rules/rules")
	private String name;

	/**
	 * 规则类型
	 */
	@SetProperty(attributeName = "type", pattern = "matching-rules/rules")
	private int type;

	/**
	 * 是否启用顺序匹配,默认按照匹配顺序依次向下匹配
	 */
	@SetProperty(attributeName = "ensort", pattern = "matching-rules/rules")
	private boolean enSort;

	/**
	 * 目标表名
	 */
	@SetProperty(attributeName = "desttablename", pattern = "matching-rules/rules")
	private String destTablename;

	/**
	 * SQL语句，当与目标表名同时存在时，默认使用表名匹配
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/sql")
	private String sql;

	/**
	 * SQL语句参数
	 */
	@BeanPropertySetter(pattern = "matching-rules/rules/params")
	private String sqlParams;

	/**
	 * 多条返回策略
	 */
	private MultiPolicy multiPolicy;
	
	/**
	 * 规则定义集合
	 */
	private List<Rule> ruleList = new ArrayList<>();

	@SetNext
	public void addBooks(Rule rule) {
		if (rule == null) {
			return;
		}
		this.ruleList.add(rule);
	}
	
	@SetNext
	public void setMultiPolicy(MultiPolicy multiPolicy) {
		if (multiPolicy == null) {
			return;
		}
		this.multiPolicy = multiPolicy;
	}
	
}
