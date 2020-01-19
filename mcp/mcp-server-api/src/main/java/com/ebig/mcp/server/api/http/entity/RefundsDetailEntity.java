package com.ebig.mcp.server.api.http.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 退货单明细信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@TableName("hdi_refunds_detail")
public class RefundsDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 退货主单id
	 */
	private Long masterId;
	/**
	 * 供货主单id
	 */
	private Long supplyMasterId;
	/**
	 * 供货细单id
	 */
	private String supplyDetailId;
	/**
	 * 供货单编号
	 */
	private String supplyno;
	/**
	 * 商品类型(1:药品;2:试剂;3:耗材)
	 */
	private Integer goodsType;
	/**
	 * 原医院商品id
	 */
	private String sourcesGoodsId;
	/**
	 * 原医院商品编码
	 */
	private String sourcesGoodsCode;
	/**
	 * 原医院商品名称
	 */
	private String sourcesGoodsName;
	/**
	 * 原医院商品规格id
	 */
	private String sourcesSpecsId;
	/**
	 * 原医院商品规格编码
	 */
	private String sourcesSpecsCode;
	/**
	 * 原医院商品规格名称
	 */
	private String sourcesSpecsName;
	/**
	 * 平台医院商品id
	 */
	private Long goodsId;
	/**
	 * 平台医院商品编码
	 */
	private String goodsCode;
	/**
	 * 平台医院商品名称
	 */
	private String goodsName;
	/**
	 * 平台医院商品规格id
	 */
	private Long specsId;
	/**
	 * 平台医院商品规格编码
	 */
	private String specsCode;
	/**
	 * 平台医院商品规格名称
	 */
	private String specsName;
	/**
	 * 生产批号id
	 */
	private Long lotId;
	/**
	 * 生产批号
	 */
	private String lotNo;
	/**
	 * 商品单位编码
	 */
	private String goodsUnitCode;
	/**
	 * 申请退货数量
	 */
	private Integer applyRefundsNumber;
	/**
	 * 实际退货数量
	 */
	private Integer realityRefundsNumber;
	/**
	 * 退货单价
	 */
	private BigDecimal refundsPrice;
	/**
	 * 退货原因
	 */
	private String refundsRemark;
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
	 * 原数据id（临时表退货主单id）
	 */
	private String orgdataId;
	/**
	 * 原数据细单id（临时表退货细单id）
	 */
	private String orgdatadtlId;

	public String getOrgdatadtlId() {
		return orgdatadtlId;
	}

	public void setOrgdatadtlId(String orgdatadtlId) {
		this.orgdatadtlId = orgdatadtlId;
	}

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
	 * 设置：退货主单id
	 */
	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}
	/**
	 * 获取：退货主单id
	 */
	public Long getMasterId() {
		return masterId;
	}
	/**
	 * 设置：供货主单id
	 */
	public void setSupplyMasterId(Long supplyMasterId) {
		this.supplyMasterId = supplyMasterId;
	}
	/**
	 * 获取：供货主单id
	 */
	public Long getSupplyMasterId() {
		return supplyMasterId;
	}
	/**
	 * 设置：供货细单id
	 */
	public void setSupplyDetailId(String supplyDetailId) {
		this.supplyDetailId = supplyDetailId;
	}
	/**
	 * 获取：供货细单id
	 */
	public String getSupplyDetailId() {
		return supplyDetailId;
	}
	/**
	 * 设置：供货单编号
	 */
	public void setSupplyno(String supplyno) {
		this.supplyno = supplyno;
	}
	/**
	 * 获取：供货单编号
	 */
	public String getSupplyno() {
		return supplyno;
	}
	/**
	 * 设置：商品类型(1:药品;2:试剂;3:耗材)
	 */
	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}
	/**
	 * 获取：商品类型(1:药品;2:试剂;3:耗材)
	 */
	public Integer getGoodsType() {
		return goodsType;
	}
	/**
	 * 设置：原医院商品id
	 */
	public void setSourcesGoodsId(String sourcesGoodsId) {
		this.sourcesGoodsId = sourcesGoodsId;
	}
	/**
	 * 获取：原医院商品id
	 */
	public String getSourcesGoodsId() {
		return sourcesGoodsId;
	}
	/**
	 * 设置：原医院商品编码
	 */
	public void setSourcesGoodsCode(String sourcesGoodsCode) {
		this.sourcesGoodsCode = sourcesGoodsCode;
	}
	/**
	 * 获取：原医院商品编码
	 */
	public String getSourcesGoodsCode() {
		return sourcesGoodsCode;
	}
	/**
	 * 设置：原医院商品名称
	 */
	public void setSourcesGoodsName(String sourcesGoodsName) {
		this.sourcesGoodsName = sourcesGoodsName;
	}
	/**
	 * 获取：原医院商品名称
	 */
	public String getSourcesGoodsName() {
		return sourcesGoodsName;
	}
	/**
	 * 设置：原医院商品规格id
	 */
	public void setSourcesSpecsId(String sourcesSpecsId) {
		this.sourcesSpecsId = sourcesSpecsId;
	}
	/**
	 * 获取：原医院商品规格id
	 */
	public String getSourcesSpecsId() {
		return sourcesSpecsId;
	}
	/**
	 * 设置：原医院商品规格编码
	 */
	public void setSourcesSpecsCode(String sourcesSpecsCode) {
		this.sourcesSpecsCode = sourcesSpecsCode;
	}
	/**
	 * 获取：原医院商品规格编码
	 */
	public String getSourcesSpecsCode() {
		return sourcesSpecsCode;
	}
	/**
	 * 设置：原医院商品规格名称
	 */
	public void setSourcesSpecsName(String sourcesSpecsName) {
		this.sourcesSpecsName = sourcesSpecsName;
	}
	/**
	 * 获取：原医院商品规格名称
	 */
	public String getSourcesSpecsName() {
		return sourcesSpecsName;
	}
	/**
	 * 设置：平台医院商品id
	 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	/**
	 * 获取：平台医院商品id
	 */
	public Long getGoodsId() {
		return goodsId;
	}
	/**
	 * 设置：平台医院商品编码
	 */
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	/**
	 * 获取：平台医院商品编码
	 */
	public String getGoodsCode() {
		return goodsCode;
	}
	/**
	 * 设置：平台医院商品名称
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	/**
	 * 获取：平台医院商品名称
	 */
	public String getGoodsName() {
		return goodsName;
	}
	/**
	 * 设置：平台医院商品规格id
	 */
	public void setSpecsId(Long specsId) {
		this.specsId = specsId;
	}
	/**
	 * 获取：平台医院商品规格id
	 */
	public Long getSpecsId() {
		return specsId;
	}
	/**
	 * 设置：平台医院商品规格编码
	 */
	public void setSpecsCode(String specsCode) {
		this.specsCode = specsCode;
	}
	/**
	 * 获取：平台医院商品规格编码
	 */
	public String getSpecsCode() {
		return specsCode;
	}
	/**
	 * 设置：平台医院商品规格名称
	 */
	public void setSpecsName(String specsName) {
		this.specsName = specsName;
	}
	/**
	 * 获取：平台医院商品规格名称
	 */
	public String getSpecsName() {
		return specsName;
	}
	/**
	 * 设置：生产批号id
	 */
	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}
	/**
	 * 获取：生产批号id
	 */
	public Long getLotId() {
		return lotId;
	}
	/**
	 * 设置：生产批号
	 */
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	/**
	 * 获取：生产批号
	 */
	public String getLotNo() {
		return lotNo;
	}
	/**
	 * 设置：商品单位编码
	 */
	public void setGoodsUnitCode(String goodsUnitCode) {
		this.goodsUnitCode = goodsUnitCode;
	}
	/**
	 * 获取：商品单位编码
	 */
	public String getGoodsUnitCode() {
		return goodsUnitCode;
	}
	/**
	 * 设置：申请退货数量
	 */
	public void setApplyRefundsNumber(Integer applyRefundsNumber) {
		this.applyRefundsNumber = applyRefundsNumber;
	}
	/**
	 * 获取：申请退货数量
	 */
	public Integer getApplyRefundsNumber() {
		return applyRefundsNumber;
	}
	/**
	 * 设置：实际退货数量
	 */
	public void setRealityRefundsNumber(Integer realityRefundsNumber) {
		this.realityRefundsNumber = realityRefundsNumber;
	}
	/**
	 * 获取：实际退货数量
	 */
	public Integer getRealityRefundsNumber() {
		return realityRefundsNumber;
	}
	/**
	 * 设置：退货单价
	 */
	public void setRefundsPrice(BigDecimal refundsPrice) {
		this.refundsPrice = refundsPrice;
	}
	/**
	 * 获取：退货单价
	 */
	public BigDecimal getRefundsPrice() {
		return refundsPrice;
	}
	/**
	 * 设置：退货原因
	 */
	public void setRefundsRemark(String refundsRemark) {
		this.refundsRemark = refundsRemark;
	}
	/**
	 * 获取：退货原因
	 */
	public String getRefundsRemark() {
		return refundsRemark;
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
}
