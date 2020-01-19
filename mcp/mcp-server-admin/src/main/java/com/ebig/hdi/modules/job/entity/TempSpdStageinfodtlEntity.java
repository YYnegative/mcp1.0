package com.ebig.hdi.modules.job.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * SpdStageInfodtl
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-07-12 10:35:00
 */
@TableName("temp_spd_stageinfodtl")
@Data
public class TempSpdStageinfodtlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 跟台目录明细标识
	 */
	@TableId
	private String stageinfodtlid;
	/**
	 * 跟台目录标识
	 */
	private String stageinfoid;
	/**
	 * 商品标识
	 */
	private String goodsid;
	/**
	 * 商品单位标识
	 */
	private String unitid;
	/**
	 * 批次标识
	 */
	private String batchid;
	/**
	 * 批号标识
	 */
	private String plotid;
	/**
	 * 批号
	 */
	private String plotno;
	/**
	 * 批号生产日期
	 */
	private Date pproddate;
	/**
	 * 批号有效期
	 */
	private Date pvalidto;
	/**
	 * 灭菌批号标识
	 */
	private String slotid;
	/**
	 * 灭菌批号
	 */
	private String slotno;
	/**
	 * 灭菌日期
	 */
	private Date sproddate;
	/**
	 * 灭菌有效期
	 */
	private Date svalidto;
	/**
	 * 批次价
	 */
	private BigDecimal batchprice;
	/**
	 * 配送数量
	 */
	private BigDecimal deliveryqty;
	/**
	 * 验收数量
	 */
	private BigDecimal acceptqty;
	/**
	 * 厂家码
	 */
	private String factoryno;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 跟台目录明细状态
	 */
	private BigDecimal stageinfodtlstatus;
	/**
	 * 
	 */
	private Integer udflag;

}
