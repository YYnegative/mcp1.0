package com.ebig.hdi.modules.license.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理商证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
@TableName("hdi_license_agent_info")
@Data
public class LicenseAgentInfoEntity implements Serializable {
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
	 * 代理商id
	 */
	private Long agentId;
	/**
	 * 分类id
	 */
	private Long classifyId;
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
	 * 新证照id(换证)
	 */
	private Long newLicenseId;

	
}
