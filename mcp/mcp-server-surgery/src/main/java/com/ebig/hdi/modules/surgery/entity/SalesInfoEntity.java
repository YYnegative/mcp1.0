package com.ebig.hdi.modules.surgery.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 销售表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@TableName("mcp_sales_info")
@Data
public class SalesInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 销售id
	 */
	@TableId
	private Long id;
	/**
	 * 销售单号
	 */
	private String salesNo;
	/**
	 * 所属机构
	 */
	private Long deptId;
	/**
	 * 销方id
	 */
	private Long sellerId;
	/**
	 * 销方名称
	 */
	private String sellerName;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 来源id
	 */
	private Long sourceId;
	/**
	 * 来源单号
	 */
	private String sourceNo;
	/**
	 * 销售类型(1:药品;2:耗材;3:试剂;4:医院跟台)
	 */
	private Integer salesType;
	/**
	 * 销售状态(0:冻结;1:初始;2:确认;3:已完成;)
	 */
	private Integer status;
	/**
	 * 销售人员
	 */
	private String salesName;
	/**
	 * 销售时间
	 */
	private Date salesTime;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 备注
	 */
	private String memo;

}
