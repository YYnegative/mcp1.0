package com.ebig.hdi.modules.license.entity.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.license.entity.LicenseAgentInfoEntity;

import lombok.Data;

/**
 * 代理商证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
@Data
public class LicenseAgentInfoVO extends LicenseAgentInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 供应商名称
	 */
	private String supplierName;
	
	/**
	 * 代理商名称
	 */
	private String agentName;
	
	/**
	 * 统一社会信用代码
	 */
	private String creditCode;
	
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
	private Integer IsWarning;
	
	/**
	 * 预警天数
	 */
	private Integer earlyDate;
	
	private Integer loseTime;
	
	/**
	 * 证照状态
	 */
	private Integer licenseStatus;
	
	/**
	 * 证照名称/编号
	 */
	private String nameOrNumber;
	
	/**
	 * 接收前端的开始时间
	 */
	private String beginTimeStr;
	
	/**
	 * 接收前端的结束时间
	 */
	private String endTimeStr;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	private String fileterDept;
	
	/**
	 * 是否默认排序，1：是
	 */
	private Integer isDefaultOrder;
	
	/**
	 * 父证照
	 */
	private LicenseAgentInfoVO parentAgentLicense;
	
	/**
	 * 子证照
	 */
	private LicenseAgentInfoVO childAgentLicense;
}
