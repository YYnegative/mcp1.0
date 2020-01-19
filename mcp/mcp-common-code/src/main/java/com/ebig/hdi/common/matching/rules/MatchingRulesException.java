package com.ebig.hdi.common.matching.rules;

/**
 * 自定义异常
 * @author zack
 *
 */
@SuppressWarnings("serial")
public class MatchingRulesException extends RuntimeException{

	public MatchingRulesException() {
		
	}
	
	public MatchingRulesException(String errorMsg) {
		super(errorMsg);
	}
	
	public MatchingRulesException(Exception e) {
		super(e);
	}
	
	public MatchingRulesException(String errorMsg, Exception e) {
		super(errorMsg, e);
	}
}
