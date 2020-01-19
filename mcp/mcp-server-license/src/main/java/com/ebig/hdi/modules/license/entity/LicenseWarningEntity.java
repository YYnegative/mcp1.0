package com.ebig.hdi.modules.license.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 证照预警信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-28 09:22:55
 */
@Data
public class LicenseWarningEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)
	 */
	private Integer licenseType;
	/**
	 * 分类id
	 */
	private Long classifyId;
	/**
	 * 分类名称
	 */
	private String classifyName;
	/**
	 * 证照名称
	 */
	private String name;
	/**
	 * 证照编号
	 */
	private String number;
	/**
	 * 效期开始时间
	 */
	private Date beginTime;
	/**
	 * 效期结束时间
	 */
	private Date endTime;
	/**
	 * 证照图片
	 */
	private String picUrl;
	/**
	 * 状态(0:停用;1:启用)
	 */
	private Integer status;
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
	private Integer delFlag;
	/**
	 * 新证照id(换证)
	 */
	private Long newLicenseId;
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
	/**
	 * 所属机构
	 */
	private Long deptId;
}
