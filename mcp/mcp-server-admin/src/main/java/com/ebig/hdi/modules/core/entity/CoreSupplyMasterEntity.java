package com.ebig.hdi.modules.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
 * 
 * @author frink
 * @email 
 * @Timestamp 2019-05-26 18:26:03
 */
@Data
@TableName("hdi_core_supply_master")
public class CoreSupplyMasterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 供货单标识
	 */
	@TableId
	private Long supplyMasterId;
	/**
	 * 供货单编号
	 */
	private String supplyno;
	/**
	 * 销售单号
	 */
	private String salno;
	/**
	 * 原供应商id
	 */
	private String sourcesSupplierId;
	/**
	 * 原供应商编码
	 */
	private String sourcesSupplierCode;
	/**
	 * 原供应商名称
	 */
	private String sourcesSupplierName;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 供应商编码
	 */
	private String supplierCode;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 原医院id
	 */
	private String sourcesHospitalId;
	/**
	 * 原医院编码
	 */
	private String sourcesHospitalCode;
	/**
	 * 原医院名称
	 */
	private String sourcesHospitalName;
	/**
	 * 机构标识（部门id）
	 */
	private Long deptId;
	/**
	 * 平台医院id
	 */
	private Long horgId;
	/**
	 * 平台医院编码
	 */
	private String hospitalCode;
	/**
	 * 平台医院名称
	 */
	private String hospitalName;
	/**
	 * 原医院库房id
	 */
	private String sourcesStorehouseId;
	/**
	 * 平台医院库房标识id
	 */
	private Long storehouseid;
	/**
	 * 平台医院库房编码
	 */
	private String storehouseNo;
	/**
	 * 平台医院库房名称
	 */
	private String storehouseName;
	/**
	 * 采购主单id
	 */
	private Long purchaseMasterId;
	/**
	 * 采购计划编号
	 */
	private String purplanno;
	/**
	 * 供货时间
	 */
	private Timestamp supplyTime;
	/**
	 * 供货类型 0 非票货同行 1 票货同行
	 */
	private Integer supplyType;
	/**
	 * 供货地址
	 */
	private String supplyAddr;
	/**
	 * 供货状态 0未提交 1 已提交 2 已验收 3 部分验收 4 拒收
	 */
	private Integer supplyStatus;
	/**
	 * ERP出库单
	 */
	private Long sourceid;
	/**
	 * 原数据标识
	 */
	private String orgdataid;
	/**
	 * 数据来源 1手工 2 接口
	 */
	private Integer datasource;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 0否 -1是
	 */
	private Integer delFlag;
	/**
	 * 创建人标识
	 */
	private Long cremanid;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改时间
	 */
	private Timestamp editTime;
	/**
	 * 创建时间
	 */
	private Timestamp credate;

	/**
	 * 采购细单标识
	 */
	@TableField(exist=false)
	private Long purchaseDetailId;

	/**
	 * 预计到货时间
	 */
	private Timestamp expectTime;

	/**
	 * 库房名称
	 */
//	@TableField(exist=false)
//	private String storehousename;
	/**
	 * 医院id
	 */
	@TableField(exist=false)
	private Long id;
	
}
