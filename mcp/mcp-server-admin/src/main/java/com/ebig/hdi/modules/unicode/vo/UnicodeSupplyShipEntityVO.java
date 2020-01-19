package com.ebig.hdi.modules.unicode.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;

import lombok.Data;

@Data
public class UnicodeSupplyShipEntityVO extends UnicodeSupplyShipEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 平台供应商名称
	 */
	private String supplierName;
	
	/**
	 * 平台供应商社会信用代码
	 */
	private String supplierCreditCode;
	
	/**
	 * 平台医院名称
	 */
	private String hospitalName;
	
	/**
	 * 平台医院社会信用代码
	 */
	private String hospitalCreditCode;

}
