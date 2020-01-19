package com.ebig.hdi.modules.surgery.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 销售明细表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@TableName("mcp_sales_detail_info")
@Data
public class SalesDetailInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 销售明细id
	 */
	@TableId
	private Long id;
	/**
	 * 销售id
	 */
	private Long salesId;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品规格id
	 */
	private Long goodsSpecsId;
	/**
	 * 商品规格
	 */
	private String goodsSpecs;
	/**
	 * 单位
	 */
	private String goodsUnit;
	/**
	 * 批号id
	 */
	private Long plotId;
	/**
	 * 批号
	 */
	private String plotNo;
	/**
	 * 批号生产日期
	 */
	private Date plotProddate;
	/**
	 * 批号效期
	 */
	private Date plotValidto;
	/**
	 * 灭菌批号id
	 */
	private Long slotId;
	/**
	 * 灭菌批号
	 */
	private String slotNo;
	/**
	 * 灭菌批号生产日期
	 */
	private Date slotProddate;
	/**
	 * 灭菌批号效期
	 */
	private Date slotValidto;
	/**
	 * 数量
	 */
	private Double salesQuantity;
	/**
	 * 单价
	 */
	private Double salesPrice;

}
