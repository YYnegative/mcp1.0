package com.ebig.hdi.modules.license.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.ebig.hdi.modules.license.entity.LicenseFactoryInfoEntity;

import lombok.Data;

/**
 * 厂商证照信息包装类
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-05-27 09:24:40
 */
@Data
public class LicenseFactoryInfoEntityVo extends LicenseFactoryInfoEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 厂商名称
	 */
	@NotBlank
	private String factoryName;
	/**
	 * 证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)
	 */
	private Integer licenseType;
	/**
	 * 分类名称
	 */
	private String classifyName;
	/**
	 * 是否预警(0:否;1:是)
	 */
	private Integer isWarning;
	/**
	 * 预警天数
	 */
	private Integer earlyDate;
	/**
	 * 距离失效天数
	 */
	private Integer loseTime;
	/**
	 * 证照状态
	 */
	private Integer licenseStatus;

}
