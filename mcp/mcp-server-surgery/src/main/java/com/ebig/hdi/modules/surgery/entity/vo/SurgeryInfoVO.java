package com.ebig.hdi.modules.surgery.entity.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.surgery.entity.SurgeryInfoEntity;

import lombok.Data;

/**
 * 手术信息表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@Data
public class SurgeryInfoVO extends SurgeryInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 医院名称
	 */
	private String hospitalName;
	
	/**
	 * 手术开始时间str
	 */
	private String surgeryDateBeginStr;
	
	/**
	 * 手术结束时间str
	 */
	private String surgeryDateEndStr;
	
	/**
	 * 是否默认排序，1：是
	 */
	private Integer isDefaultOrder;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	private String fileterDept;
}
