package com.ebig.mcp.server.api.http.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 退货单信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@TableName("hdi_refunds_master")
public class RefundsMasterEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 退货单编号
	 */
	private String refundsNo;
	/**
	 * 原供应商id
	 */
	private String sourcesSupplierId;
	/**
	 * 原供应商编码
	 */
	private String sourcesSupplierCode;
	/**
	 * 原供应商名称
	 */
	private String sourcesSupplierName;
	/**
	 * 平台供应商id
	 */
	private Long supplierId;
	/**
	 * 平台供应商编码
	 */
	private String supplierCode;
	/**
	 * 平台供应商名称
	 */
	private String supplierName;
	/**
	 * 原医院id
	 */
	private String sourcesHospitalId;
	/**
	 * 原医院编码
	 */
	private String sourcesHospitalCode;
	/**
	 * 原医院名称
	 */
	private String sourcesHospitalName;
	/**
	 * 平台医院id
	 */
	private Long hospitalId;
	/**
	 * 平台医院编码
	 */
	private String hospitalCode;
	/**
	 * 平台医院名称
	 */
	private String hospitalName;
	/**
	 * 原医院库房id
	 */
	private String sourcesStoreHouseId;
	/**
	 * 医院库房id
	 */
	private Long storeHouseId;
	/**
	 * 医院库房编码
	 */
	private String storehouseNo;
	/**
	 * 医院库房名称
	 */
	private String storehouseName;
	/**
	 * 医院退货申请单号
	 */
	private String refundsApplyNo;
	/**
	 * 消退单号
	 */
	private String regressionNumber;
	/**
	 * 退货时间
	 */
	private Date refundsTime;
	/**
	 * 状态(0:未确认;1:已提交;2:已完成)
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
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource;
	/**
	 * 原数据id（临时表退货主单id）
	 */
	private String orgdataId;

	public String getOrgdataId() {
		return orgdataId;
	}

	public void setOrgdataId(String orgdataId) {
		this.orgdataId = orgdataId;
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
	 * 设置：退货单编号
	 */
	public void setRefundsNo(String refundsNo) {
		this.refundsNo = refundsNo;
	}
	/**
	 * 获取：退货单编号
	 */
	public String getRefundsNo() {
		return refundsNo;
	}
	/**
	 * 设置：原供应商id
	 */
	public void setSourcesSupplierId(String sourcesSupplierId) {
		this.sourcesSupplierId = sourcesSupplierId;
	}
	/**
	 * 获取：原供应商id
	 */
	public String getSourcesSupplierId() {
		return sourcesSupplierId;
	}
	/**
	 * 设置：原供应商编码
	 */
	public void setSourcesSupplierCode(String sourcesSupplierCode) {
		this.sourcesSupplierCode = sourcesSupplierCode;
	}
	/**
	 * 获取：原供应商编码
	 */
	public String getSourcesSupplierCode() {
		return sourcesSupplierCode;
	}
	/**
	 * 设置：原供应商名称
	 */
	public void setSourcesSupplierName(String sourcesSupplierName) {
		this.sourcesSupplierName = sourcesSupplierName;
	}
	/**
	 * 获取：原供应商名称
	 */
	public String getSourcesSupplierName() {
		return sourcesSupplierName;
	}
	/**
	 * 设置：平台供应商id
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	/**
	 * 获取：平台供应商id
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**
	 * 设置：平台供应商编码
	 */
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	/**
	 * 获取：平台供应商编码
	 */
	public String getSupplierCode() {
		return supplierCode;
	}
	/**
	 * 设置：平台供应商名称
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	/**
	 * 获取：平台供应商名称
	 */
	public String getSupplierName() {
		return supplierName;
	}
	/**
	 * 设置：原医院id
	 */
	public void setSourcesHospitalId(String sourcesHospitalId) {
		this.sourcesHospitalId = sourcesHospitalId;
	}
	/**
	 * 获取：原医院id
	 */
	public String getSourcesHospitalId() {
		return sourcesHospitalId;
	}
	/**
	 * 设置：原医院编码
	 */
	public void setSourcesHospitalCode(String sourcesHospitalCode) {
		this.sourcesHospitalCode = sourcesHospitalCode;
	}
	/**
	 * 获取：原医院编码
	 */
	public String getSourcesHospitalCode() {
		return sourcesHospitalCode;
	}
	/**
	 * 设置：原医院名称
	 */
	public void setSourcesHospitalName(String sourcesHospitalName) {
		this.sourcesHospitalName = sourcesHospitalName;
	}
	/**
	 * 获取：原医院名称
	 */
	public String getSourcesHospitalName() {
		return sourcesHospitalName;
	}
	/**
	 * 设置：平台医院id
	 */
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	/**
	 * 获取：平台医院id
	 */
	public Long getHospitalId() {
		return hospitalId;
	}
	/**
	 * 设置：平台医院编码
	 */
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	/**
	 * 获取：平台医院编码
	 */
	public String getHospitalCode() {
		return hospitalCode;
	}
	/**
	 * 设置：平台医院名称
	 */
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	/**
	 * 获取：平台医院名称
	 */
	public String getHospitalName() {
		return hospitalName;
	}
	/**
	 * 设置：原医院库房id
	 */
	public void setSourcesStoreHouseId(String sourcesStoreHouseId) {
		this.sourcesStoreHouseId = sourcesStoreHouseId;
	}
	/**
	 * 获取：原医院库房id
	 */
	public String getSourcesStoreHouseId() {
		return sourcesStoreHouseId;
	}
	/**
	 * 设置：医院库房id
	 */
	public void setStoreHouseId(Long storeHouseId) {
		this.storeHouseId = storeHouseId;
	}
	/**
	 * 获取：医院库房id
	 */
	public Long getStoreHouseId() {
		return storeHouseId;
	}
	/**
	 * 设置：医院库房编码
	 */
	public void setStorehouseNo(String storehouseNo) {
		this.storehouseNo = storehouseNo;
	}
	/**
	 * 获取：医院库房编码
	 */
	public String getStorehouseNo() {
		return storehouseNo;
	}
	/**
	 * 设置：医院库房名称
	 */
	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}
	/**
	 * 获取：医院库房名称
	 */
	public String getStorehouseName() {
		return storehouseName;
	}
	/**
	 * 设置：医院退货申请单号
	 */
	public void setRefundsApplyNo(String refundsApplyNo) {
		this.refundsApplyNo = refundsApplyNo;
	}
	/**
	 * 获取：医院退货申请单号
	 */
	public String getRefundsApplyNo() {
		return refundsApplyNo;
	}
	/**
	 * 设置：消退单号
	 */
	public void setRegressionNumber(String regressionNumber) {
		this.regressionNumber = regressionNumber;
	}
	/**
	 * 获取：消退单号
	 */
	public String getRegressionNumber() {
		return regressionNumber;
	}
	/**
	 * 设置：退货时间
	 */
	public void setRefundsTime(Date refundsTime) {
		this.refundsTime = refundsTime;
	}
	/**
	 * 获取：退货时间
	 */
	public Date getRefundsTime() {
		return refundsTime;
	}
	/**
	 * 设置：状态(0:未确认;1:已提交;2:已完成)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0:未确认;1:已提交;2:已完成)
	 */
	public Integer getStatus() {
		return status;
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
}
