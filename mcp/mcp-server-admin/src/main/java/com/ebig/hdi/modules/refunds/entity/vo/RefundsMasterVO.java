package com.ebig.hdi.modules.refunds.entity.vo;

import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 退货单信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-28 16:24:21
 */
@Data
public class RefundsMasterVO extends RefundsMasterEntity implements Serializable {
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
	 * 退货申请单的申请时间
	 */
	private Date applyTime;

	/**
	 * 退货时间string
	 */
	private String refundsTimeStr;

	/**
	 * 退货开始时间string
	 */
	private String refundsTimeBeginStr;

	/**
	 * 退货结束时间string
	 */
	private String refundsTimeEndStr;

	/**
	 * 细单条目
	 */
	private Integer detailNumber;

	/**
	 * 退货单详情list
	 */
	private List<RefundsDetailEntity> refundsDetailList;

	/**
	 * 退货单详情VOlist
	 */
	private List<RefundsDetailVO> refundsDetailVOList;

	/**
	 * 是否默认排序，1：是
	 */
	private Integer isDefaultOrder;

	/**
	 * 分页时候的部门权限过滤
	 */
	private String fileterDept;
	

}
