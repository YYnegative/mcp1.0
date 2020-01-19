package com.ebig.hdi.common.matching.rules;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.binder.DigesterLoadingException;

import lombok.Data;

/**
 * 匹配规则
 * 
 * @author zack
 *
 */
@Data
// matching-rules标签和MatchingRules对象映射
@ObjectCreate(pattern = "matching-rules")
public class MatchingRules {

	private Map<String, Rules> rules = new LinkedHashMap<>();

	// 将matching-rules/rules标签的内容对象添加到MatchingRules对象中
	@SetNext
	public void addBooks(Rules rules) {
		if (rules == null) {
			return;
		}
		if (this.rules.containsKey(rules.getName())) {
			throw new DigesterLoadingException("规则名重复，解析失败");
		}
		this.rules.put(rules.getName(), rules);
	}
}
