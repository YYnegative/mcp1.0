package com.ebig.hdi.modules.job.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 供应商证照信息临时表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-07-10 19:36:44
 */
@TableName("temp_spd_license_supplier")
@Data
public class TempSpdLicenseSupplierEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private String id;
	/**
	 * 供应商id
	 */
	private String supplierId;
	/**
	 * 分类id
	 */
	private String classifyId;
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
	private BigDecimal status;
	/**
	 * 所属机构(医院id)
	 */
	private String deptId;
	/**
	 * 创建人id
	 */
	private String createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人id
	 */
	private String editId;
	/**
	 * 修改时间
	 */
	private Date editTime;
	/**
	 * 是否删除(-1:已删除;0:正常)
	 */
	private BigDecimal delFlag;
	/**
	 * 新证照id(换证)
	 */
	private String newLicenseId;
}
