package com.ebig.hdi.modules.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
@Data
@TableName("hdi_core_label_master")
public class CoreLabelMasterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签主键
	 */
	@TableId
	private Long labelid;
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 标签编码
	 */
	private String labelno;
	/**
	 * 医院机构标识-平台医院id
	 */
	private Long horgId;
	/**
	 * 库房标识
	 */
	private Long storehouseid;
	/**
	 * 标签状态
	 */
	private Integer labelstatus;
	/**
	 * 来源标识--供货主单id
	 */
	private Long sourceid;
	/**
	 * 创建人标识
	 */
	private Long cremanid;

	/**
	 * 创建时间
	 */
	private Timestamp credate;
	/**
	 * 0否 -1是
	 */
	private Integer delFlag;
	/**
	 * 标签图片地址
	 */
	private String imageUrl;
	
	/**
	 * 供应商名称
	 */
	private String supplierName;

	//---------以下为新增字段-------
	/**
	 * 供应商编码
	 */
	private String supplierCode;

	/**
	 * 原供应商id
	 */
	private String sourcesSupplierId;
	/**4
	 * 原供应商编码
	 */
	private String sourcesSupplierCode;
	/**
	 * 原供应商名称
	 */
	private String sourcesSupplierName;
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
	 * 平台医院库房编码
	 */
	private String storehouseNo;
	/**
	 * 平台医院库房名称
	 */
	private String storehouseName;


	/**
	 * 创建人
	 */
	@TableField(exist=false)
	private String cremanname;
	/**
	 * 供货单编号
	 */
	@TableField(exist=false)
	private String supplyno;
	/**
	 * 供货单时间
	 */
	@TableField(exist=false)
	private Timestamp supplyTime;
	
	/**
	 * 库房名称
	 */
	@TableField(exist=false)
	private String storehousename;

	/**
	 * 供货类型
	 */
	@TableField(exist=false)
	private Integer supplyType;
}
