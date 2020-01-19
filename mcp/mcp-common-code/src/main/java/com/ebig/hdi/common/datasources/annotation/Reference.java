package com.ebig.hdi.common.datasources.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户描述主外键关系
 * @author zack
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Reference {

	/**
	 * 涉及的表的实体类
	 * @return
	 */
	Class<?> value();
	
	/**
	 * 涉及的表的实体类关联属性,如不填，则默认为是主键
	 * @return
	 */
	String field() default "";
}
