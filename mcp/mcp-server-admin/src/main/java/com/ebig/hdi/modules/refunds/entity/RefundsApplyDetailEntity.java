package com.ebig.hdi.modules.refunds.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 退货申请单明细信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-31 17:23:00
 */
@TableName("hdi_refunds_apply_detail")
@Data
public class RefundsApplyDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 退货申请主单id
	 */
	private Long applyMasterId;
	/**
	 * 验证主单id
	 */
	private Long acceptMasterId;
	/**
	 * 验收单编号
	 */
	private String acceptNo;
	/**
	 * 验收明细单id
	 */
	private Long acceptDetailId;
	/**
	 * 商品类型(1:药品;2:试剂;3:耗材)
	 */
	private Integer goodsType;
	/**
	 * 医院商品id
	 */
	private Long goodsId;
	/**
	 * 原医院商品id
	 */
	private String sourcesGoodsId;
	/**
	 * 医院商品规格id
	 */
	private Long specsId;
	/**
	 * 原医院商品规格id
	 */
	private String sourcesSpecsId;
	/**
	 * 生产批号id
	 */
	private Long lotId;
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
	 * 供货单价
	 */
	private BigDecimal supplyPrice;
	/**
	 * 退货原因
	 */
	private String refundsRemark;
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
	@TableLogic
	private Integer delFlag;

	
}
