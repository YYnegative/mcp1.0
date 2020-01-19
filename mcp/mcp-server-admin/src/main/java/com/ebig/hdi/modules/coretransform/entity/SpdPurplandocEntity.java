package com.ebig.hdi.modules.coretransform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-08-19 17:46:38
 */
@TableName("spd_purplandoc")
@Data
public class SpdPurplandocEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采购计划标识
	 */
	@TableId
	private String purplandocid;
	/**
	 * 库房科室标识
	 */
	private String storehouseid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 采购计划编号
	 */
	private String purplanno;
	/**
	 * 配送商标识
	 */
	private String supplyid;
	/**
	 * 配送商编号
	 */
	private String supplyno;
	/**
	 * 配送商名称
	 */
	private String supplyname;
	/**
	 * 预计到货时间
	 */
	private Date anticipate;
	/**
	 * 采购订单状态
	 */
	private BigDecimal purplanstatus;
	/**
	 * 总金额
	 */
	private BigDecimal totalmoney;
	/**
	 * 计划方式
	 */
	private BigDecimal purplanmode;
	/**
	 * 工作流实例
	 */
	private String pinstanceid;
	/**
	 * 工作流状态
	 */
	private BigDecimal workflowstatus;
	/**
	 * 原数据
	 */
	private String orgdataid;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 创建人ID
	 */
	private String cremanid;
	/**
	 * 创建人名称
	 */
	private String cremanname;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 审核时间
	 */
	private Date auditdate;
	/**
	 * 审核人ID
	 */
	private String auditmanid;
	/**
	 * 审核人
	 */
	private String auditmanname;
	/**
	 * 库房地址标识
	 */
	private String shaddressid;
	/**
	 * 流程实例
	 */
	private String processInstanceId;
	/**
	 * 发送
	 */
	private BigDecimal send;
	/**
	 * 发货
	 */
	private BigDecimal delivery;
	/**
	 * 完成
	 */
	private BigDecimal finish;
	/**
	 * 
	 */
	private BigDecimal genhpurorder;
	/**
	 * 采购计划完成状态
	 */
	private BigDecimal purplanfinishstatus;
	/**
	 * 跟台标志
	 */
	private BigDecimal stageflag;
	/**
	 * 提交时间
	 */
	private Date commitdate;

}
