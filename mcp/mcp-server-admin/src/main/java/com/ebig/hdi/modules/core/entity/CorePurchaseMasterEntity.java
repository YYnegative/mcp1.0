package com.ebig.hdi.modules.core.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-08-29 14:39:32
 */
@Data
@TableName("hdi_core_purchase_master")
public class CorePurchaseMasterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采购单标识
	 */
	@TableId
	private Long purchaseMasterId;
	/**
	 * 采购计划编号
	 */
	private String purplanno;
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
	 * 平台供应商id
	 */
	private Long supplierId;
	/**
	 * 平台供应商编码
	 */
	private String supplierCode;
	/**
	 * 平台供应商名称
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
	 * 平台库房id
	 */
	private Long storehouseid;
	/**
	 * 库房编码
	 */
	private String storehouseNo;
	/**
	 * 库房名称
	 */
	private String storehouseName;
	/**
	 * 采购时间
	 */
	private Date purplantime;
	/**
	 * 预计到货时间
	 */
	private Date expecttime;
	/**
	 * 采购单状态(0:已作废;1:未确认;2:已确认;3:已供货;4:部分供货)
	 */
	private Integer purchasestatus;
	/**
	 * 供货地址
	 */
	private String supplyAddr;
	/**
	 * 原采购单id
	 */
	private String orgdataid;
	/**
	 * 数据来源标识
	 */
	private String sourceid;
	/**
	 * 数据来源(1:手工;2: 接口)
	 */
	private Integer datasource;
	/**
	 * 供应商所属机构
	 */
	private Long deptId;
	/**
	 * 是否删除(-1:已删除;0:正常)
	 */
	private Integer delFlag;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 创建人id
	 */
	private Long cremanid;
	/**
	 * 创建时间
	 */
	private Timestamp credate;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改时间
	 */
	private Timestamp editTime;

	/**
	 * 细单数量
	 */
	@TableField(exist=false)
	private Integer detailNumber;

	/**
	 * 采购时间起始时间
	 */
	@TableField(exist=false)
	private String purplanStartingTime;
	/**
	 * 采购时间结束时间
	 */
	@TableField(exist=false)
	private String purplanEndTime;
	/**
	 * 预计到货时间起始时间
	 */
	@TableField(exist=false)
	private String expectStartingTime;
	/**
	 * 预计到货时间结束时间
	 */
	@TableField(exist=false)
	private String expectEndTime;

	/**
	 * 采购医院商品的id标识
	 */
	@TableField(exist=false)
	private Long hgoodsid;
	/**
	 * 采购医院商品规格标识
	 */
	@TableField(exist=false)
	private Long hgoodstypeid;
	/**
	 * 采购医院商品编码
	 */
	@TableField(exist=false)
	private String hgoodsno;


	/**
	 * 采购供应商商品的id标识
	 */
	@TableField(exist=false)
	private Long sgoodsid;
	/**
	 * 采购供应商商品规格标识
	 */
	@TableField(exist=false)
	private Long sgoodstypeid;
	/**
	 * 采购供应商商品编码
	 */
	@TableField(exist=false)
	private String sgoodsno;

	/**
	 * 库房名称
	 */
	@TableField(exist=false)
	private String storehousename;
	/**
	 * 库房地址
	 */
	@TableField(exist=false)
	private String shaddress;
	/**
	 * 医院id
	 */
	@TableField(exist=false)
	private Long id;
}
