package com.ebig.hdi.modules.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 验收单
 * 
 * @author frink
 * @email 
 * @date 2019-05-31 15:29:11
 */
@Data
@TableName("hdi_core_accept_master")
public class CoreAcceptMasterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 验收单标识
	 */
	@TableId
	private Long acceptMasterId;
	/**
	 * 验收单编号
	 */
	private String acceptno;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 医院机构标识
	 */
	private Long horgId;
	/**
	 * 原医院机构标识
	 */
	private String uorganid;
	/**
	 * HIS原供应商标识
	 */
	private String hisSupplyid;
	/**
	 * 库房标识
	 */
	private Long storehouseid;
	/**
	 * 原库房标识
	 */
	private String ystorehouseid;
	/**
	 * 数据来源 1手工 2 接口
	 */
	private Integer datasource;
	/**
	 * 供货单标识
	 */
	private Long sourceid;
	/**
	 * 原始数据标识
	 */
	private String orgdataid;
	/**
	 * 结算标志 0 未结算 1 已结算
	 */
	private Integer settleFlag;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 创建人标识
	 */
	private Long cremanid;
	/**
	 * 创建人
	 */
//	private String cremanname;
	/**
	 * 创建时间
	 */
	private Timestamp credate;
	/**
	 * 0否 -1是
	 */
	private Integer delFlag;

	//---------改造后新增字段------

	/**
	 * 原供应商编码
	 */
	private String sourcesSupplierCode;
	/**
	 * 原供应商名称
	 */
	private String sourcesSupplierName;
	/**
	 * 供应商编码
	 */
	private String supplierCode;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 原医院编码
	 */
	private String sourcesHospitalCode;
	/**
	 * 原医院名称
	 */
	private String sourcesHospitalName;
	/**
	 * 平台医院编码
	 */
	private String hospitalCode;
	/**
	 * 平台医院名称
	 */
	private String hospitalName;
	/**
	 * 平台医院库房编码
	 */
	private String storehouseNo;
	/**
	 * 平台医院库房名称
	 */
	private String storehouseName;



	/**
	 * 供货单编号
	 */
	@TableField(exist=false)
	private String supplyno;
	
	/**
	 * 库房名称
	 */
//	@TableField(exist=false)
//	private String storehousename;
}
