package com.ebig.hdi.modules.refunds.entity.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.refunds.entity.RefundsApplyDetailEntity;

import lombok.Data;

/**
 * 退货申请单明细信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
@Data
public class RefundsApplyDetailVO extends RefundsApplyDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 厂商名称
	 */
	private String factoryName;
	
	/**
	 * 商品规格名称
	 */
	private String specsName;
	
	/**
	 * 生成批号名称
	 */
	private String lotName;
	
	/**
	 * 商品单位名称
	 */
	private String goodsUnitName;
}
