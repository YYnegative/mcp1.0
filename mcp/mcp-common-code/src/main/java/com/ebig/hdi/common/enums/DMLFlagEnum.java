package com.ebig.hdi.common.enums;

import com.ebig.hdi.common.datasources.annotation.DMLFlag;
import com.ebig.hdi.common.utils.ReflectUitls;

import lombok.Getter;

/**
 * DML标记枚举
 * 
 * @author zack
 *
 */
public enum DMLFlagEnum {

	INSERT(1), UPDATE(2), DELETE(3);

	@Getter
	private Integer type;

	private DMLFlagEnum(Integer type) {
		this.type = type;
	}

	/**
	 * 根据DMLFlag注解标记获取DML类型
	 * @param t
	 * @return
	 */
	public static <T> Integer getType(T t) {
		Object fieldValue = ReflectUitls.getFieldValue(t, ReflectUitls.getAnnotationField(t, DMLFlag.class));
		if (fieldValue instanceof Integer) {
			return (Integer) fieldValue;
		}
		return null;
	}
}
