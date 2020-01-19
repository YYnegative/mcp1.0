package com.ebig.hdi.modules.job.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * SpdStageInfo
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-07-12 10:34:59
 */
@TableName("temp_spd_stageinfo")
@Data
public class TempSpdStageinfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 跟台目录标识
	 */
	private String stageinfoid;
	/**
	 * 跟台目录编号
	 */
	private String stageinfono;
	/**
	 * 机构标识
	 */
	private String uorganid;
	/**
	 * 手术单标识
	 */
	private String surgeryid;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 来源编号
	 */
	private String sourceno;
	/**
	 * 供应商标识
	 */
	private String supplyid;
	/**
	 * 供应商编号
	 */
	private String supplyno;
	/**
	 * 供应商名称
	 */
	private String supplyname;
	/**
	 * 跟台目录状态
	 */
	private BigDecimal stageinfostatus;
	/**
	 * 跟台目录类型
	 */
	private BigDecimal stageinfotype;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 创建人ID
	 */
	private String cremanid;
	/**
	 * 创建人名称
	 */
	private String cremanname;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 修改人ID
	 */
	private String editmanid;
	/**
	 * 修改人
	 */
	private String editmanname;
	/**
	 * 修改日期
	 */
	private Date editdate;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 
	 */
	private Integer udflag;

}
