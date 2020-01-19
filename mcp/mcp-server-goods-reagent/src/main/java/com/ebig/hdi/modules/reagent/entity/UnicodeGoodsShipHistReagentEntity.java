package com.ebig.hdi.modules.reagent.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
@Data
@TableName("hdi_unicode_goods_ship_hist")
public class UnicodeGoodsShipHistReagentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 匹配历史标识
	 */
	@TableId
	private Long shiphistId;
	/**
	 * 匹配标识
	 */
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
	 * 操作类型 1匹对 2商品信息变更
	 */
	private Integer operType;
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
	private String hospitalName;
	/**
	 * 耗材名称
	 */
	private String goodsName;
	/**
	 * 生产厂商名称
	 */
	private String factoryName;
	/**
	 * 商品规格
	 */
	private String specs;
	/**
	 * 批准文号
	 */
	private String approvals;
	/**
	 * 平台商品名称
	 */
	private String preagentName;
	/**
	 * 平台商品id
	 */
	private Long id;
	/**
	 * 平台商品属性
	 */
	private Integer goodsNature;
	/**
	 * 平台商品模块
	 */
	private String cateName;

	/**
	 * 流程实例id
	 */
	private String processId;

}
