package com.ebig.hdi.modules.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.common.datasources.annotation.DMLFlag;
import com.ebig.hdi.common.datasources.annotation.Reference;

import lombok.Data;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
@Data
@TableName("hdi_core_label_detail")
public class CoreLabelDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签明细标识
	 */
	@TableId
	private Long labeldtlid;
	/**
	 * 标签标识
	 */
	@Reference(CoreLabelMasterEntity.class)
	private Long labelid;
	/**
	 * 标签数量/供货数量
	 */
	private Double labelQty;
	/**
	 * 供货单标识
	 */
	private Long supplyMasterId;
	/**
	 * 供货细单标识
	 */
	private Long supplyDetailId;

	
	/**
	 * 生产厂家名称
	 */
	@TableField(exist=false)
	private String factoryName;
	/**
	 * 采购商品名称
	 */
	@TableField(exist=false)
	private String goodsName;
	/**
	 * 商品规格
	 */
	@TableField(exist=false)
	private String goodstype;
	/**
	 * 商品单位
	 */
	@TableField(exist=false)
	private String goodsunit;
	/**
	 * 生产批号
	 */
	@TableField(exist=false)
	private String lotno;
	/**
	 * 生产日期
	 */
	@TableField(exist=false)
	private Timestamp proddate;
	/**
	 * 失效日期
	 */
	@TableField(exist=false)
	private Timestamp invadate;
	
	/**
	 * dml标记 1新增 2更新 3删除
	 */
	@DMLFlag
	@TableField(exist=false)
	private Integer dmlFlag;
	
}
