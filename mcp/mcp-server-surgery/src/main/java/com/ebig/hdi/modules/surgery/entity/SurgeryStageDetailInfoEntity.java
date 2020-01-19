package com.ebig.hdi.modules.surgery.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 手术跟台目录明细表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@TableName("mcp_surgery_stage_detail_info")
@Data
public class SurgeryStageDetailInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 跟台目录明细id
	 */
	@TableId
	private Long id;
	/**
	 * 原跟台目录细单id
	 */
	private String sourceStageDetailId;
	/**
	 * 跟台目录id
	 */
	private Long surgeryStageId;
	/**
	 * 跟台耗材id
	 */
	private Long consumablesId;
	/**
	 * 跟台耗材规格id
	 */
	private Long consumablesSpecsId;
	/**
	 * 批号id
	 */
	private Long plotId;
	/**
	 * 批号
	 */
	private String plotNo;
	/**
	 * 批号生产日期
	 */
	private Date plotProddate;
	/**
	 * 批号失效日期
	 */
	private Date plotValidto;
	/**
	 * 灭菌批号id
	 */
	private Long slotId;
	/**
	 * 灭菌批号
	 */
	private String slotNo;
	/**
	 * 灭菌批号生产日期
	 */
	private Date slotProddate;
	/**
	 * 灭菌批号失效日期
	 */
	private Date slotValidto;
	/**
	 * 厂家id
	 */
	private Long factoryId;
	/**
	 * 厂家码
	 */
	private String factoryNo;
	/**
	 * 单价
	 */
	private Double price;
	/**
	 * 数量
	 */
	private Double consumablesQuantity;
	/**
	 * 数据来源(1:手工;2:供应商;)
	 */
	private Integer dataSource;

}
