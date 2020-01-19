package com.ebig.hdi.modules.reagent.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-21 15:18:03
 */
@Data
@TableName("hdi_unicode_goods_ship")
public class UnicodeGoodsShipReagentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 关系标识
	 */
	@TableId(type = IdType.INPUT)
	private Long shipId;
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 目标机构标识
	 */
	private Long torgId;
	/**
	 * 目标机构类型
	 */
	private Integer torgType;
	/**
	 * 1 药品 2 试剂 3 耗材
	 */
	private Integer tgoodsType;
	/**
	 * 目标商品标识
	 */
	private Long tgoodsId;
	/**
	 * 平台商品标识
	 */
	private Long pgoodsId;
	/**
	 * 目标商品规格标识
	 */
	private Long tspecsId;
	/**
	 * 平台商品规格标识
	 */
	private Long pspecsId;
	/**
	 * 目标商品认证编号标识
	 */
	private Long tapprovalId;
	/**
	 * 平台商品认证编号标识
	 */
	private Long papprovalId;
	/**
	 * 0 未匹配 1已匹配
	 */
	private Integer shipFlag;
	/**
	 * 审核状态
	 */
	private Integer checkStatus;
	/**
	 * 0否 1是
	 */
	private Integer delFlag;
	/**
	 * 创建人标识
	 */
	private Long cremanid;
	/**
	 * 创建人
	 */
	private String cremanname;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 修改人标识
	 */
	private Long editmanid;
	/**
	 * 修改人
	 */
	private String editmanname;
	/**
	 * 修改时间
	 */
	private Date editdate;
	/**
	 * 备注
	 */
	private String memo;

	/**
	 * 医院名称
	 */
	@TableField(exist=false)
	private String hospitalName;
	/**
	 * 耗材名称
	 */
	@TableField(exist=false)
	private String goodsName;
	/**
	 * 生产厂商名称
	 */
	@TableField(exist=false)
	private String factoryName;
	/**
	 * 商品规格
	 */
	@TableField(exist=false)
	private String specs;
	/**
	 * 批准文号
	 */
	@TableField(exist=false)
	private String approvals;
	/**
	 * 商品全球唯一码
	 */
	@TableField(exist=false)
	private String guid;
	/**
	 * 平台商品名称
	 */
	@TableField(exist=false)
	private String preagentName;
	/**
	 * 平台商品id
	 */
	@TableField(exist=false)
	private Long id;
	/**
	 * 平台商品属性
	 */
	@TableField(exist=false)
	private Integer goodsNature;
	/**
	 * 平台商品模块
	 */
	@TableField(exist=false)
	private String cateName;
	/**
	 * 商品、规格信息
	 */
	@TableField(exist=false)
	private String nameSpecs;
	
	public String getNameSpecs(){
		return this.goodsName + this.specs;
	}
	
	/**
	 * 唯一ID
	 */
	@TableField(exist=false)
	private Long onlyId;
	
	/**
	 * 供应商名称信息
	 */
	@TableField(exist=false)
	private String supplierName;
	/**
	 * 平台商品规格
	 */
	@TableField(exist=false)
	private String pspecs;
	
}
