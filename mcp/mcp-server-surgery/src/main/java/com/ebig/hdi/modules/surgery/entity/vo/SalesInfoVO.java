package com.ebig.hdi.modules.surgery.entity.vo;

import java.io.Serializable;
import java.util.List;

import com.ebig.hdi.modules.surgery.entity.SalesDetailInfoEntity;
import com.ebig.hdi.modules.surgery.entity.SalesInfoEntity;

import lombok.Data;

/**
 * 销售表VO
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@Data
public class SalesInfoVO extends SalesInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String hospitalName;
	
	/**
	 * 医院清台结算单编号
	 */
	
	private String surgeryTitle;
	
	private String customName;
	
	/**
	 * 销售时间str
	 */
	private String salesTimeStr;
	
	/**
	 * 明细条目
	 */
	private Integer detailNumber;
	
	/**
	 * 是否默认排序，1：是
	 */
	private Integer isDefaultOrder;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	private String fileterDept; 
	
	private List<SalesDetailInfoVO> salesDetailInfoVOList;
	
	private List<SalesDetailInfoEntity> salesDetailInfoList;
	
}
