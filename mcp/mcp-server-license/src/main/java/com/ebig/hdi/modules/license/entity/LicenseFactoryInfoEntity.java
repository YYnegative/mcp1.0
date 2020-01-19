package com.ebig.hdi.modules.license.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 厂商证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
@Data
@TableName("hdi_license_factory_info")
public class LicenseFactoryInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 供应商id
	 */
	@NotNull(message = "供应商id不能为空")
	private Long supplierId;
	/**
	 * 厂商id
	 */
	private Long factoryId;
	/**
	 * 分类id
	 */
	@NotNull(message = "分类id不能为空")
	private Long classifyId;
	/**
	 * 证照名称
	 */
	@NotBlank(message = "证照名称不能为空")
	private String name;
	/**
	 * 证照编号
	 */
	@NotBlank(message = "证照编号不能为空")
	private String number;
	/**
	 * 效期开始时间
	 */
	@NotNull(message = "效期开始时间不能为空")
	private Date beginTime;
	/**
	 * 效期结束时间
	 */
	@NotNull(message = "效期结束时间不能为空")
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
	private Integer delFlag;
	/**
	 * 新证照id(换证)
	 */
	private Long newLicenseId;
	
}
