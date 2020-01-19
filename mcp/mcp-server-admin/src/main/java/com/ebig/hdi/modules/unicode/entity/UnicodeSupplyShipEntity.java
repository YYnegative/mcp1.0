package com.ebig.hdi.modules.unicode.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;

import lombok.Data;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-17 11:52:52
 */
@Data
@TableName("hdi_unicode_supply_ship")
public class UnicodeSupplyShipEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 关系标识
	 */
	@TableId
	private Long shipId;
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 平台医院供应商关系id
	 */
	@NotNull(message="平台医院供应商关系id", groups= {UpdateGroup.class})
	private Long supplierHospitalRefId;
	/**
	 * 平台医院机构id
	 */
	@NotNull(message="平台医院机构id", groups= {UpdateGroup.class})
	private Long hospitalId;
	/**
	 * 平台供应商id
	 */
	@NotNull(message="平台供应商id", groups= {UpdateGroup.class})
	private Long supplierId;
	/**
	 * 原系统医院供应商关系id
	 */
	private String sourcesShipId;
	/**
	 * 原系统医院供应商id
	 */
	private String sourcesSupplierId;
	/**
	 * 原系统医院供应商名称
	 */
	@NotBlank(message = "医院供应商名称不能为空", groups= {AddGroup.class})
	private String sourcesSupplierName;
	/**
	 * 原系统医院供应商社会信用代码
	 */
	@NotBlank(message = "医院供应商社会信用代码不能为空", groups= {AddGroup.class})
	private String sourcesSupplierCreditCode;
	/**
	 * 原系统医院id
	 */
	private String sourcesHospitalId;
	/**
	 * 原系统医院名称
	 */
	@NotBlank(message = "医院名称不能为空", groups= {AddGroup.class})
	private String sourcesHospitalName;
	/**
	 * 原系统医院社会信用代码
	 */
	@NotBlank(message = "医院社会信用代码不能为空", groups= {AddGroup.class})
	private String sourcesHospitalCreditCode;
	/**
	 * 0 未匹配 1已匹配
	 */
	private Integer shipFlag;
	/**
	 * 审核状态
	 */
	private Integer checkStatus;
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
	private Date credate;
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
	private Date editdate;
	/**
	 * 0手动录入 1接口
	 */
	private Integer datasource;
	/**
	 * 是否删除(-1:已删除;0:正常)
	 */
	@TableLogic
	private Integer delFlag;
	/**
	 * 备注
	 */
	private String memo;
}
