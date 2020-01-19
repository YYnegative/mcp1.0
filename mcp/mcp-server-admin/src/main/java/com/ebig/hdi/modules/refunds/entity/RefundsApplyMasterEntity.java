package com.ebig.hdi.modules.refunds.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 退货申请单信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-31 17:23:00
 */
@TableName("hdi_refunds_apply_master")
@Data
public class RefundsApplyMasterEntity implements Serializable {
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
	 * 原系统医院供应商id
	 */
	private String sourcesSupplierId;
	/**
	 * 医院id
	 */
	private Long hospitalId;
	/**
	 * 原系统医院id
	 */
	private String sourcesHospitalId;
	/**
	 * 医院库房id
	 */
	private Long storeHouseId;
	/**
	 * 原系统医院库房id
	 */
	private String sourcesStoreHouseId;
	/**
	 * 医院退货申请单号
	 */
	private String refundsApplyNo;
	/**
	 * 申请时间
	 */
	private Date applyTime;
	/**
	 * 状态(0:未确认;1:已作废;2:已确认;3:已退货;4:部分退货)
	 */
	private Integer status;
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
	 * 是否删除(-1:已删除;0:正常)
	 */
	@TableLogic
	private Integer delFlag;
	/**
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource;

}
