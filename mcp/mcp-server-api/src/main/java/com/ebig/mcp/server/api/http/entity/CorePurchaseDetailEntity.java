package com.ebig.mcp.server.api.http.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.common.datasources.annotation.DMLFlag;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-08-29 14:39:32
 */
@Data
@TableName("hdi_core_purchase_detail")
public class CorePurchaseDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采购细单标识
	 */
	@TableId
	private Long purchaseDetailId;
	/**
	 * 采购单标识
	 */
	private Long purchaseMasterId;
	/**
	 * 商品类别(1:药品;2:试剂;3:耗材)
	 */
	private Integer goodsclass;
	/**
	 * 原医院商品id
	 */
	private String yhgoodsid;
	/**
	 * 原医院商品名称
	 */
	private String yhgoodsname;
	/**
	 * 原医院商品规格id
	 */
	private String yhgoodstypeid;
	/**
	 * 原医院商品规格编码
	 */
	private String yhgoodsno;
	/**
	 * 原医院商品规格名称
	 */
	private String yhgoodstypename;
	/**
	 * 医院采购商品单位
	 */
	private String hgoodsunit;
	/**
	 * 采购单价
	 */
	private Double hunitprice;
	/**
	 * 医院采购数量
	 */
	private Double hqty;
	/**
	 * 平台医院商品标识
	 */
	private Long hgoodsid;
	/**
	 * 平台医院商品编码
	 */
	private String hgoodsno;
	/**
	 * 平台医院商品名称
	 */
	private String hgoodsname;
	/**
	 * 平台医院商品规格标识
	 */
	private Long hgoodstypeid;
	/**
	 * 平台医院商品规格编码
	 */
	private String hgoodstypeno;
	/**
	 * 平台医院商品规格名称
	 */
	private String hgoodstype;
	/**
	 * 原数据标识
	 */
	private String orgdataid;
	/**
	 * 原始明细数据标识
	 */
	private String orgdatadtlid;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 来源明细标识
	 */
	private String sourcedtlid;
	/**
	 * 备注
	 */
	private String memo;
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
	* 供应商商品标识
	*/
	@TableField(exist=false)
	private Long sgoodsid;
	/**
	 * 供应商商品名称
	 */
	@TableField(exist=false)
	private String sgoodsname;
	/**
	 * 供应商商品编码
	 */
	@TableField(exist=false)
	private String sgoodsno;
	/**
	 * 供应商商品规格标识
	 */
	@TableField(exist=false)
	private Long sgoodstypeid;
	/**
	 * 供应商商品规格
	 *
	 */
	@TableField(exist=false)
	private String sgoodstype;

	/**
	 * 供应商商品规格编码
	 */
	@TableField(exist=false)
	private String sgoodstypeno;
	/**
	 * 供应商商品单位
	 */
	@TableField(exist=false)
	private String sgoodsunit;

	/**
	 * 供货数量
	 */
	@TableField(exist=false)
	private Double supplyQty;

	/**

	/**
	 * dml标记 1新增 2更新 3删除
	 */
	@DMLFlag
	@TableField(exist=false)
	private Integer dmlFlag;


	/**
	 * 生产厂家名称
	 */
	@TableField(exist=false)
	private String factoryName;
	/**
	 * 采购商品名称
	 */
	@TableField(exist=false)
	private String goodsName;
	/**
	 * 生产厂家id
	 */
	@TableField(exist=false)
	private Long factoryId;
	/**
	 * 供应商商品生产厂家id
	 */
	@TableField(exist=false)
	private Long sfactoryId;
	/**
	 * 供应商商品生产厂家名称
	 */
	@TableField(exist=false)
	private String sfactoryName;
}
