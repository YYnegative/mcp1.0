package com.ebig.mcp.server.api.http.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.common.datasources.annotation.DMLFlag;
import com.ebig.hdi.common.datasources.annotation.Reference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
@Data
@TableName("hdi_core_supply_detail")
public class CoreSupplyDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 供货细单标识
	 */
	@TableId
	private Long supplyDetailId;
	/**
	 * 供货单标识
	 */
	@Reference(CoreSupplyMasterEntity.class)
	private Long supplyMasterId;
	/**
	 * 采购细单标识
	 */
	private Long purchaseDetailId;
	/**
	 * 采购单标识
	 */
	private Long purchaseMasterId;
	/**
	 * ERP出库单标识
	 */
	private Long sourceid;
	/**
	 * ERP出库明细标识
	 */
	private Long sourcedtlid;
	/**
	 * 采购计划标识
	 */
	private String purplanid;
	/**
	 * 采购明细标识
	 */
	private String purplandtlid;
	/**
	 * 商品标识
	 */
	private Long goodsid;
	/**
	 * 1 药品 2 试剂 3 耗材
	 */
	private Integer goodsclass;
	/**
	 * 商品编码
	 */
	private String goodsno;
	/**
	 * 采购商品名称
	 */
	private String goodsname;
	/**
	 * 商品规格标识
	 */
	private Long goodstypeid;
	/**
	 * 商品规格
	 */
	private String goodstype;
	/**
	 * 商品规格编码
	 */
	private String goodstypeno;
	/**
	 * 商品单位
	 */
	private String goodsunit;
	/**
	 * 供货数量
	 */
	private Double supplyQty;
	/**
	 * 供货单价
	 */
	private Double supplyUnitprice;
	/**
	 * 批号标识
	 */
	private Long lotid;
	/**
	 * 生产批号
	 */
	private String lotno;
	/**
	 * 生产日期
	 */
	private Timestamp proddate;
	/**
	 * 失效日期
	 */
	private Timestamp invadate;
	/**
	 * 商品批次编码
	 */
	private String batchCode;
	/**
	 * 商品批次图片地址
	 */
	private String imageUrl;
	/**
	 * 创建人标识
	 */
	private Long cremanid;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 原数据标识
	 */
	private String orgdataid;
	/**
	 * 原始明细数据标识
	 */
	private String orgdatadtlid;
	/**
	 * 创建时间
	 */
	private Timestamp credate;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改时间
	 */
	private Timestamp editTime;


	/**
	 * dml标记 1新增 2更新 3删除
	 */
	@DMLFlag
	@TableField(exist=false)
	private Integer dmlFlag;
	/**
	 * 创建人名称
	 */
	@TableField(exist=false)
	private String cremanname;

	/**
	 * 总价
	 */
	@TableField(exist=false)
	private String totalAmount;

	/**
	 * 匹配标志 0 未匹配 1 已匹配 默认0
	 */
	@TableField(exist=false)
	private Integer shipFlag;
	/**
	 * 生产厂家名称
	 */
	@TableField(exist=false)
	private String factoryName;

	/**
	 * 生产厂家id
	 */
	@TableField(exist=false)
	private Long factoryId;


	/**
	 * 标签数量/包装数量
	 */
	@TableField(exist=false)
	private Double labelQty;
	/**
	 * 未包装数量
	 */
	@TableField(exist=false)
	private Double unpackagedNumber;
	
	
	/**
	 * 设置：生产日期
	 */
	public void setProddate(Timestamp proddate) {
		this.proddate = proddate;
	}
	/**
	 * 获取：生产日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getProddate() {
		return proddate;
	}
	/**
	 * 设置：失效日期
	 */
	public void setInvadate(Timestamp invadate) {
		this.invadate = invadate;
	}
	/**
	 * 获取：失效日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public Date getInvadate() {
		return invadate;
	}
	
	/**
	 * 打印批次码数量
	 */
	@TableField(exist=false)
	private Integer printAmount;

}
