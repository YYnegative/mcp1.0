package com.ebig.hdi.modules.org.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 供应商信息待审批表
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-15 09:24:04
 */
@TableName("hdi_org_supplier_info_approval")
public class OrgSupplierInfoApprovalEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 流程实例id
	 */
	private String processId;
	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 父供应商id
	 */
	private Long parentId;
	/**
	 * 供应商编码
	 */
	private String supplierCode;
	/**
	 * 供应商名称
	 */
	private String supplierName;
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
	 * 供应商地址
	 */
	private String supplierAddress;
	/**
	 * 状态(0:停用;1:启用)
	 */
	private Integer status;
	/**
	 * 供应商性质(0:国营企业;1:民营企业;2:中外合资企业;3:股份制企业;4:个体企业)
	 */
	private Integer supplierNature;
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
	 * 是否集团机构(0:否;1:是)
	 */
	private Integer isGroup;
	/**
	 * 子机构数(集团供应商允许创建的子供应商数)
	 */
	private Integer childNumber;
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
	/**
	 * 是否删除(-1:已删除;0:正常)
	 */
	@TableLogic
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
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;

	/**
	 * 传给前端和id一样的属性
	 */
	@TableField(exist=false)
	private String value;

	@TableField(exist=false)
	private List<OrgSupplierInfoApprovalEntity> list;

	@TableField(exist=false)
	private String parentName;

	/**
	 * 是否默认排序，1：是
	 */
	@TableField(exist=false)
	private Integer isDefaultOrder;

	/**
	 * 分页时候的部门权限过滤
	 */
	@TableField(exist=false)
	private String fileterDept;

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<OrgSupplierInfoApprovalEntity> getList() {
		return list;
	}

	public void setList(List<OrgSupplierInfoApprovalEntity> list) {
		this.list = list;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getIsDefaultOrder() {
		return isDefaultOrder;
	}

	public void setIsDefaultOrder(Integer isDefaultOrder) {
		this.isDefaultOrder = isDefaultOrder;
	}

	public String getFileterDept() {
		return fileterDept;
	}

	public void setFileterDept(String fileterDept) {
		this.fileterDept = fileterDept;
	}

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
	 * 设置：父供应商id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：父供应商id
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：供应商编码
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	/**
	 * 获取：供应商编码
	 */
	public String getSupplierCode() {
		return supplierCode;
	}
	/**
	 * 设置：供应商名称
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	/**
	 * 获取：供应商名称
	 */
	public String getSupplierName() {
		return supplierName;
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
	 * 设置：供应商地址
	 */
	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}
	/**
	 * 获取：供应商地址
	 */
	public String getSupplierAddress() {
		return supplierAddress;
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
	 * 设置：供应商性质(0:国营企业;1:民营企业;2:中外合资企业;3:股份制企业;4:个体企业)
	 */
	public void setSupplierNature(Integer supplierNature) {
		this.supplierNature = supplierNature;
	}
	/**
	 * 获取：供应商性质(0:国营企业;1:民营企业;2:中外合资企业;3:股份制企业;4:个体企业)
	 */
	public Integer getSupplierNature() {
		return supplierNature;
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
	 * 设置：子机构数(集团供应商允许创建的子供应商数)
	 */
	public void setChildNumber(Integer childNumber) {
		this.childNumber = childNumber;
	}
	/**
	 * 获取：子机构数(集团供应商允许创建的子供应商数)
	 */
	public Integer getChildNumber() {
		return childNumber;
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
