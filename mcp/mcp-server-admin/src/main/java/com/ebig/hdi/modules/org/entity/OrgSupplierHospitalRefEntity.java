package com.ebig.hdi.modules.org.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商医院绑定关系
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-24 09:12:17
 */
@Data
@TableName("hdi_org_supplier_hospital_ref")
public class OrgSupplierHospitalRefEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 医院id
	 */
	private Long hospitalId;
	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	@TableLogic
	private Integer delFlag;
	/**
	 * 所属机构
	 */
	private Long deptId;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改时间
	 */
	private Date editTime;
	/**
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource; 

}
