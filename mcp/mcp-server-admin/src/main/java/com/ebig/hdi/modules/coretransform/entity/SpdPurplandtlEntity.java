package com.ebig.hdi.modules.coretransform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-08-19 17:46:37
 */
@TableName("spd_purplandtl")
@Data
public class SpdPurplandtlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采购明细标识
	 */
	@TableId
	private String purplandtlid;
	/**
	 * 采购计划标识
	 */
	private String purplandocid;
	/**
	 * U机构标识
	 */
	private String uorganid;
	/**
	 * 医院商品标识
	 */
	private String hgoodsid;
	/**
	 * 医院商品编码
	 */
	private String hgoodsno;
	/**
	 * 医院商品规格
	 */
	private String hgoodstype;
	/**
	 * 医院商品单位
	 */
	private String hgoodsunit;
	/**
	 * 医院包装大小
	 */
	private BigDecimal hpacksize;
	/**
	 * 医院商品名称
	 */
	private String hgoodsname;
	/**
	 * 计划采购数量
	 */
	private BigDecimal purplanqty;
	/**
	 * 采购单价
	 */
	private BigDecimal purprice;
	/**
	 * 本期购进金额
	 */
	private BigDecimal purmoney;
	/**
	 * 税率
	 */
	private BigDecimal purrate;
	/**
	 * 交易平台标识
	 */
	private String tpid;
	/**
	 * 原数据
	 */
	private String orgdataid;
	/**
	 * 原数据明细标识
	 */
	private String orgdatadtlid;
	/**
	 * 数据来源
	 */
	private BigDecimal datasource;
	/**
	 * 创建时间
	 */
	private Date credate;
	/**
	 * 创建人ID
	 */
	private String cremanid;
	/**
	 * 创建人名称
	 */
	private String cremanname;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 终止时间
	 */
	private Date annultime;
	/**
	 * 终止人标识
	 */
	private String annulmanid;
	/**
	 * 终止人名称
	 */
	private String annulmanname;
	/**
	 * 计划明细状态
	 */
	private BigDecimal ppdtlstatus;
	/**
	 * 型号
	 */
	private String model;
	/**
	 * 商品规格
	 */
	private String goodstype;
	/**
	 * 厂家名称
	 */
	private String factoryname;
	/**
	 * 批号
	 */
	private String plotno;
	/**
	 * 批号有效期
	 */
	private Date pvaliddate;
	/**
	 * 商品单位
	 */
	private String hunitid;
	/**
	 * 缺省单位采购数量
	 */
	private BigDecimal defaultunitqty;
	/**
	 * 商品完成状态
	 */
	private BigDecimal goodsfinishstatus;
	/**
	 * 已配送数量
	 */
	private BigDecimal shipedqty;
	/**
	 * 已验收数量
	 */
	private BigDecimal receivedqty;

}
