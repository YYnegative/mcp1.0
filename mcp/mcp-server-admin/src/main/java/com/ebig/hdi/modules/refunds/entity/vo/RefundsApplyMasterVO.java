package com.ebig.hdi.modules.refunds.entity.vo;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsApplyMasterEntity;

import lombok.Data;

/**
 * 退货申请单信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
@Data
public class RefundsApplyMasterVO extends RefundsApplyMasterEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 医院名称
	 */
	private String hospitalName;
	
	/**
	 * 库房名称
	 */
	private String storeHouseName;
	
	/**
	 * 申请时间字符串
	 */
	private String applyTimeStr;
	
	/**
	 * 申请开始时间
	 */
	private String applyTimeBeginStr;
	
	/**
	 * 申请结束时间
	 */
	private String applyTimeEndStr;
	
	/**
	 * 细单条目
	 */
	private Integer detailNumber;
	
	/**
	 * 退货申请单详情VOlist
	 */
	private List<RefundsApplyDetailVO> detailVOList;
	
	/**
	 * 退货申请单详情list
	 */
	private List<RefundsApplyDetailEntity> detailList;
	
	/**
	 * 是否默认排序，1：是
	 */
	private Integer isDefaultOrder;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	private String fileterDept;
	
	
}
