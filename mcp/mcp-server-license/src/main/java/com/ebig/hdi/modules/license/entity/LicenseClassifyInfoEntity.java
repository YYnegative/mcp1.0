package com.ebig.hdi.modules.license.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 证照分类信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-21 16:45:19
 */
@TableName("hdi_license_classify_info")
@Data
public class LicenseClassifyInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)
	 */
	private Integer type;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 是否预警(0:否;1:是)
	 */
	private Integer isWarning;
	/**
	 * 预警天数
	 */
	private Integer earlyDate;
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

}
