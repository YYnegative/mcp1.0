package com.ebig.hdi.modules.job.entity;

import java.io.Serializable;

import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;

import lombok.Data;

@Data
public class MatchTaskEntity extends GoodsHospitalConsumablesEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 医院标识
	 */
	private Long torgId;
	/**
	 * 全球唯一码
	 */
	private String guid;
	/**
	 * 医院药品批准文号
	 */
	private String goodsApprovals;
	/**
	 * 医院药品规格
	 */
	private String specs;
	/**
	 * 商品Id
	 */
	private Long goodsId;
	/**
	 * 商品规格Id
	 */
	private Long goodsSpecsId;
	/**
	 * 商品批准文号Id
	 */
	private Long goodsApprovalsId;
	
}
