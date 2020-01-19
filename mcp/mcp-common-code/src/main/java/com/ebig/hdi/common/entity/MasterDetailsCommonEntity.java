package com.ebig.hdi.common.entity;

import java.util.List;

import lombok.Data;

/**
 * 主细表通用实体类
 */
@Data
public class MasterDetailsCommonEntity<T, M> {

	private T master;
	
	private List<M> details;
}
