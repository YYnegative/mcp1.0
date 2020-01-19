package com.ebig.hdi.modules.goods.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.goods.entity.GoodsSupplierSendEntity;

import lombok.Data;

@Data
public class GoodsSupplierSendEntityVo extends GoodsSupplierSendEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品规格
	 */
	private String specs;
	
	/**
	 * 代理商id
	 */
	private String agentId;
	
	/**
	 * 生产厂商id
	 */
	private String factoryId;
	
	/**
	 * 生产厂商
	 */
	private String factoryName;
	
	/**
	 * 医院名称
	 */
	private String hospitalName;
	
	/**
	 * 商品属性
	 */
	private Integer goodsNature;
	
	/**
	 * 剂型
	 */
	private String typeName;
	
	/**
	 * 批准文号
	 */
	private String approvals;
	
	/**
	 * 已关联证照
	 */
	private Integer licenseNumber;

}
