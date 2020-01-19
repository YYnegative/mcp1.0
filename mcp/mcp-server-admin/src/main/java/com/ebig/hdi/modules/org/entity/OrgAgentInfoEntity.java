package com.ebig.hdi.modules.org.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理商信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:41
 */
@Data
@TableName("hdi_org_agent_info")
public class OrgAgentInfoEntity implements Serializable {
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
	 * 代理商编码
	 */
	private String agentCode;
	/**
	 * 代理商名称
	 */
	private String agentName;
	/**
	 * 统一社会信用代码
	 */
	private String creditCode;
	/**
	 * 所在省编码
	 */
	private String provinceCode;
	/**
	 * 所在市编码
	 */
	private String cityCode;
	/**
	 * 所在区县编码
	 */
	private String areaCode;
	/**
	 * 代理商地址
	 */
	private String agentAddress;
	/**
	 * 状态(0:停用;1:启用)
	 */
	private Integer status;
	/**
	 * 法人代表
	 */
	private String corporate;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 邮箱地址
	 */
	private String email;
	/**
	 * 传真
	 */
	private String fax;
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

}
