package com.ebig.hdi.modules.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 库房表
 * 
 * @author frink
 * @email 
 * @date 2019-05-30 18:26:36
 */
@Data
@TableName("hdi_core_storehouse")
public class CoreStorehouseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房标识
	 */
	@TableId
	private Long storehouseid;
	/**
	 * 库房名称
	 */
	private String storehousename;
	/**
	 * 库房编码
	 */
	private String storehouseno;
	/**
	 * 原数据标识
	 */
	private String orgdataid;
	/**
	 * 原医院机构标识
	 */
	private String uorganid;
	/**
	 * 医院机构标识
	 */
	private Long horgId;
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 创建人标识
	 */
	private Long cremanid;
	/**
	 * 创建人
	 */
	private String cremanname;
	/**
	 * 创建时间
	 */
	private Timestamp credate;
	/**
	 * 修改人标识
	 */
	private Long editmanid;
	/**
	 * 修改人
	 */
	private String editmanname;
	/**
	 * 修改时间
	 */
	private Timestamp editdate;
	/**
	 * 备注
	 */
	private String memo;
	
	/**
	 * 库房地址
	 */
	private String shaddress;
	/**
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource;
	
}
