package com.ebig.hdi.modules.surgery.entity.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.surgery.entity.SalesDetailInfoEntity;

import lombok.Data;

/**
 * 销售明细表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@Data
public class SalesDetailInfoVO extends SalesDetailInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String factoryName;
	
	private String plotProddateStr;
	
	private String plotValidtoStr;
	
	
}
