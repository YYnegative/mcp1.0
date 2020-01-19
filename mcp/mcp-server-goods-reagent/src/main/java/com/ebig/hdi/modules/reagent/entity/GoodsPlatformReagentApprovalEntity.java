package com.ebig.hdi.modules.reagent.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 平台试剂信息待审批表
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-22 12:54:20
 */
@TableName("hdi_goods_platform_reagent_approval")
public class GoodsPlatformReagentApprovalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 商品统一编码
	 */
	private String goodsUnicode;
	/**
	 * 试剂编码
	 */
	private String reagentCode;
	/**
	 * 试剂名称
	 */
	private String reagentName;
	/**
	 * 通用名称
	 */
	private String commonName;
	/**
	 * 商品属性(0:国产;1:进口)
	 */
	private Integer goodsNature;
	/**
	 * 商品分类id
	 */
	private Long typeId;
	/**
	 * 生产厂商id
	 */
	private String factoryId;
	/**
	 * 批准文号
	 */
	private String approvals;
	/**
	 * 状态(0:停用;1:启用)
	 */
	private Integer status;
	/**
	 * 商品单位
	 */
	private String goodsUnit;
	/**
	 * 储存方式
	 */
	private String storeWay;
	/**
	 * 图片地址
	 */
	private String picUrl;
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
	 * 审批状态(0.待审批 1.审批通过 2.审批不通过)
	 */
	private Integer checkStatus;
	/**
	 * 流程实例id
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
	 * 设置：商品统一编码
	 */
	public void setGoodsUnicode(String goodsUnicode) {
		this.goodsUnicode = goodsUnicode;
	}
	/**
	 * 获取：商品统一编码
	 */
	public String getGoodsUnicode() {
		return goodsUnicode;
	}
	/**
	 * 设置：试剂编码
	 */
	public void setReagentCode(String reagentCode) {
		this.reagentCode = reagentCode;
	}
	/**
	 * 获取：试剂编码
	 */
	public String getReagentCode() {
		return reagentCode;
	}
	/**
	 * 设置：试剂名称
	 */
	public void setReagentName(String reagentName) {
		this.reagentName = reagentName;
	}
	/**
	 * 获取：试剂名称
	 */
	public String getReagentName() {
		return reagentName;
	}
	/**
	 * 设置：通用名称
	 */
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	/**
	 * 获取：通用名称
	 */
	public String getCommonName() {
		return commonName;
	}
	/**
	 * 设置：商品属性(0:国产;1:进口)
	 */
	public void setGoodsNature(Integer goodsNature) {
		this.goodsNature = goodsNature;
	}
	/**
	 * 获取：商品属性(0:国产;1:进口)
	 */
	public Integer getGoodsNature() {
		return goodsNature;
	}
	/**
	 * 设置：商品分类id
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	/**
	 * 获取：商品分类id
	 */
	public Long getTypeId() {
		return typeId;
	}
	/**
	 * 设置：生产厂商id
	 */
	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}
	/**
	 * 获取：生产厂商id
	 */
	public String getFactoryId() {
		return factoryId;
	}
	/**
	 * 设置：批准文号
	 */
	public void setApprovals(String approvals) {
		this.approvals = approvals;
	}
	/**
	 * 获取：批准文号
	 */
	public String getApprovals() {
		return approvals;
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
	 * 设置：商品单位
	 */
	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}
	/**
	 * 获取：商品单位
	 */
	public String getGoodsUnit() {
		return goodsUnit;
	}
	/**
	 * 设置：储存方式
	 */
	public void setStoreWay(String storeWay) {
		this.storeWay = storeWay;
	}
	/**
	 * 获取：储存方式
	 */
	public String getStoreWay() {
		return storeWay;
	}
	/**
	 * 设置：图片地址
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	/**
	 * 获取：图片地址
	 */
	public String getPicUrl() {
		return picUrl;
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
	 * 设置：审批状态(0.待审批 1.审批通过 2.审批不通过)
	 */
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
	/**
	 * 获取：审批状态(0.待审批 1.审批通过 2.审批不通过)
	 */
	public Integer getCheckStatus() {
		return checkStatus;
	}
	/**
	 * 设置：流程实例id
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	/**
	 * 获取：流程实例id
	 */
	public String getProcessId() {
		return processId;
	}
}
