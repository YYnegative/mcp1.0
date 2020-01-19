package com.ebig.hdi.common.matching.rules;

import java.io.InputStream;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * 规则解析类
 * @author zack
 */
@Component("matchingRulesParser")
public class MatchingRulesParser implements InitializingBean{

	@Getter
	private MatchingRules matchingRules;

	@Override
	public void afterPropertiesSet() throws Exception {
		Digester digester = getLoader(MatchingRules.class).newDigester();
		InputStream stream = MatchingRulesParser.class.getResourceAsStream("/conf/matching-rules.xml");
		this.matchingRules = digester.parse(stream);
	}
	
	/**
	 * 获取Digester解析器,说明解析注解
	 * @param xmlClazz
	 * @return
	 */
	private DigesterLoader getLoader(final Class<?> xmlClazz) {
		return DigesterLoader.newLoader(new FromAnnotationsRuleModule()
        {
            @Override
            protected void configureRules()
            {
                bindRulesFrom(xmlClazz);
            }

        });
    }

}
