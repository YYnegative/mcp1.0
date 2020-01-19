package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 医院信息待审批记录表
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-11 15:32:38
 */
@TableName("hdi_org_hospital_info_approval")
public class OrgHospitalInfoApprovalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 父医院id
	 */
	private Long parentId;
	/**
	 * 医院编码
	 */
	private String hospitalCode;
	/**
	 * 医院级别(0:三级特等;1:三级甲等;2:三级乙等;3:三级丙等;4:二级甲等;5:二级乙等;6:二级丙等;7:一级甲等;8:一级乙等;9:一级丙等)
	 */
	private Integer hospitalGrade;
	/**
	 * 医院名称
	 */
	private String hospitalName;
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
	 * 状态(0:停用;1:启用)
	 */
	private Integer status;
	/**
	 * 医院地址
	 */
	private String hospitalAddress;
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
	 * 是否集团机构(0:否;1:是)
	 */
	private Integer isGroup;
	/**
	 * 子机构数(集团医院允许创建的子医院数)
	 */
	private Integer childNumber;
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
	private Integer delFlag;
	/**
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource;
	/**
	 * 审批状态
	 */
	private Integer checkStatus;


	/**
	 * 流程实例Id
	 */
	private String processId;

	/**
	 * 设置：主键id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：父医院id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父医院id
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：医院编码
	 */
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	/**
	 * 获取：医院编码
	 */
	public String getHospitalCode() {
		return hospitalCode;
	}
	/**
	 * 设置：医院级别(0:三级特等;1:三级甲等;2:三级乙等;3:三级丙等;4:二级甲等;5:二级乙等;6:二级丙等;7:一级甲等;8:一级乙等;9:一级丙等)
	 */
	public void setHospitalGrade(Integer hospitalGrade) {
		this.hospitalGrade = hospitalGrade;
	}
	/**
	 * 获取：医院级别(0:三级特等;1:三级甲等;2:三级乙等;3:三级丙等;4:二级甲等;5:二级乙等;6:二级丙等;7:一级甲等;8:一级乙等;9:一级丙等)
	 */
	public Integer getHospitalGrade() {
		return hospitalGrade;
	}
	/**
	 * 设置：医院名称
	 */
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	/**
	 * 获取：医院名称
	 */
	public String getHospitalName() {
		return hospitalName;
	}
	/**
	 * 设置：统一社会信用代码
	 */
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	/**
	 * 获取：统一社会信用代码
	 */
	public String getCreditCode() {
		return creditCode;
	}
	/**
	 * 设置：所在省编码
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	/**
	 * 获取：所在省编码
	 */
	public String getProvinceCode() {
		return provinceCode;
	}
	/**
	 * 设置：所在市编码
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * 获取：所在市编码
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * 设置：所在区县编码
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	/**
	 * 获取：所在区县编码
	 */
	public String getAreaCode() {
		return areaCode;
	}
	/**
	 * 设置：状态(0:停用;1:启用)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0:停用;1:启用)
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：医院地址
	 */
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	/**
	 * 获取：医院地址
	 */
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	/**
	 * 设置：法人代表
	 */
	public void setCorporate(String corporate) {
		this.corporate = corporate;
	}
	/**
	 * 获取：法人代表
	 */
	public String getCorporate() {
		return corporate;
	}
	/**
	 * 设置：联系电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：邮箱地址
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱地址
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置：是否集团机构(0:否;1:是)
	 */
	public void setIsGroup(Integer isGroup) {
		this.isGroup = isGroup;
	}
	/**
	 * 获取：是否集团机构(0:否;1:是)
	 */
	public Integer getIsGroup() {
		return isGroup;
	}
	/**
	 * 设置：子机构数(集团医院允许创建的子医院数)
	 */
	public void setChildNumber(Integer childNumber) {
		this.childNumber = childNumber;
	}
	/**
	 * 获取：子机构数(集团医院允许创建的子医院数)
	 */
	public Integer getChildNumber() {
		return childNumber;
	}
	/**
	 * 设置：传真
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * 获取：传真
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * 设置：所属机构
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：所属机构
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：创建人id
	 */
	public void setCreateId(Long createId) {
		this.createId = createId;
	}
	/**
	 * 获取：创建人id
	 */
	public Long getCreateId() {
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
	public void setEditId(Long editId) {
		this.editId = editId;
	}
	/**
	 * 获取：修改人id
	 */
	public Long getEditId() {
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
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：是否删除(-1:已删除;0:正常)
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：数据来源(0:系统录入;1:医院SPD)
	 */
	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * 获取：数据来源(0:系统录入;1:医院SPD)
	 */
	public Integer getDataSource() {
		return dataSource;
	}
	/**
	 * 设置：审批状态
	 */
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	/**
	 * 获取：审批状态
	 */
	public Integer getCheckStatus() {
		return checkStatus;
	}


	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
}
