package com.ebig.hdi.modules.refunds.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;

import lombok.Data;

/**
 * 退货单明细信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-31 17:23:00
 */
@Data
public class RefundsDetailVO extends RefundsDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 厂商名称
	 */
	private String factoryName;
	
	/**
	 * 商品规格名称
	 */
	private String specsName;
	
	/**
	 * 生成批号名称
	 */
	private String lotName;
	
	/**
	 * 商品单位名称
	 */
	private String goodsUnitName;

}
