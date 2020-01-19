package com.ebig.hdi.modules.surgery.entity.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SurgeryStageInfoEntity;

import lombok.Data;

/**
 * 手术跟台目录表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:31
 */
@Data
public class SurgeryStageInfoVO extends SurgeryStageInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 医院名称
	 */
	private String hospitalName;
	
	/**
	 * 医院手术单编号
	 */
	private String surgeryNo;
	
	/**
	 * 手术名称
	 */
	private String surgeryTitle;
	
	/**
	 * 患者名称
	 */
	private String customName;
	
	/**
	 * 手术时间
	 */
	private Date surgeryDate;
	
	/**
	 * 手术时间str
	 */
	private String surgeryDateStr;
	
	/**
	 * 明细条目
	 */
	private Integer detailNumber;
	
	private List<SurgeryStageDetailInfoEntity> surgeryStageDetailInfoEntityList;
	
	private List<SurgeryStageDetailInfoVO> surgeryStageDetailInfoVOList;
	
	/**
	 * 是否默认排序，1：是
	 */
	private Integer isDefaultOrder;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	private String fileterDept; 

}
