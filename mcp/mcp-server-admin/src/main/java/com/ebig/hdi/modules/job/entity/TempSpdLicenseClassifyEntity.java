package com.ebig.hdi.modules.job.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 证照分类信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-07-10 19:36:44
 */
@TableName("temp_spd_license_classify")
public class TempSpdLicenseClassifyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private String id;
	/**
	 * 证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)
	 */
	private BigDecimal type;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 是否预警(0:否;1:是)
	 */
	private BigDecimal isWarning;
	/**
	 * 预警天数
	 */
	private BigDecimal earlyDate;
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
	 * 设置：主键id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：主键id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)
	 */
	public void setType(BigDecimal type) {
		this.type = type;
	}
	/**
	 * 获取：证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)
	 */
	public BigDecimal getType() {
		return type;
	}
	/**
	 * 设置：分类名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：分类名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：是否预警(0:否;1:是)
	 */
	public void setIsWarning(BigDecimal isWarning) {
		this.isWarning = isWarning;
	}
	/**
	 * 获取：是否预警(0:否;1:是)
	 */
	public BigDecimal getIsWarning() {
		return isWarning;
	}
	/**
	 * 设置：预警天数
	 */
	public void setEarlyDate(BigDecimal earlyDate) {
		this.earlyDate = earlyDate;
	}
	/**
	 * 获取：预警天数
	 */
	public BigDecimal getEarlyDate() {
		return earlyDate;
	}
	/**
	 * 设置：状态(0:停用;1:启用)
	 */
	public void setStatus(BigDecimal status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0:停用;1:启用)
	 */
	public BigDecimal getStatus() {
		return status;
	}
	/**
	 * 设置：所属机构(医院id)
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：所属机构(医院id)
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * 设置：创建人id
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	/**
	 * 获取：创建人id
	 */
	public String getCreateId() {
		return createId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改人id
	 */
	public void setEditId(String editId) {
		this.editId = editId;
	}
	/**
	 * 获取：修改人id
	 */
	public String getEditId() {
		return editId;
	}
	/**
	 * 设置：修改时间
	 */
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getEditTime() {
		return editTime;
	}
	/**
	 * 设置：是否删除(-1:已删除;0:正常)
	 */
	public void setDelFlag(BigDecimal delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：是否删除(-1:已删除;0:正常)
	 */
	public BigDecimal getDelFlag() {
		return delFlag;
	}
}
