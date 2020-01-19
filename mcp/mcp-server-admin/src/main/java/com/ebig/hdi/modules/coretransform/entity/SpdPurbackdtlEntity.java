package com.ebig.hdi.modules.coretransform.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购退货明细
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-10-21 15:10:37
 */
@TableName("spd_purbackdtl")
@Data
public class SpdPurbackdtlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 采退明细标识
	 */
	@TableId
	private String purbackdtlid;
	/**
	 * 采退单标识
	 */
	private String purbackid;
	/**
	 * 商品
	 */
	private String goodsid;
	/**
	 * 商品单位名称
	 */
	private String goodsunit;
	/**
	 * 商品单位
	 */
	private String unitid;
	/**
	 * 退货数量
	 */
	private BigDecimal purbackqty;
	/**
	 * 单价
	 */
	private BigDecimal unitprice;
	/**
	 * 生产批号
	 */
	private String plotid;
	/**
	 * 灭菌批号
	 */
	private String slotid;
	/**
	 * 批次
	 */
	private String batchid;
	/**
	 * 退货原因
	 */
	private String backreason;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 机构名称
	 */
	private String uorganid;
	/**
	 * 原始标识
	 */
	private String originid;
	/**
	 * 原始单号
	 */
	private String originno;
	/**
	 * 来源标识
	 */
	private String sourceid;
	/**
	 * 来源明细标识
	 */
	private String sourcedtlid;
	/**
	 * 来源单号
	 */
	private String sourceno;
	/**
	 * 原始细单标识
	 */
	private String origindtlid;
	/**
	 * 原数据明细标识
	 */
	private String orgdatadtlid;
	/**
	 * 原数据
	 */
	private String orgdataid;
	/**
	 * 单位数量
	 */
	private BigDecimal unitqty;
	/**
	 * 传送确认标识
	 */
	private BigDecimal confirmflag;
	/**
	 * 上传下载标记，0上传 1下载
	 */
	private Integer udflag;

}
