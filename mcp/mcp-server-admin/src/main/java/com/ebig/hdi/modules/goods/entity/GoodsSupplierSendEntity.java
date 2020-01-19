package com.ebig.hdi.modules.goods.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 供应商品下发
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-07-26 11:49:04
 */
@Data
@TableName("hdi_goods_supplier_send")
public class GoodsSupplierSendEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 供应商id
	 */
	@NotNull(message = "供应商id不能为空")
	private Long supplierId;
	/**
	 * 医院id
	 */
	@NotNull(message = "医院id不能为空")
	private Long hospitalId;
	/**
	 * 商品类别(1:药品;2:试剂;3:耗材)
	 */
	@NotNull(message = "商品类别不能为空")
	private Integer goodsType;
	/**
	 * 商品id
	 */
	@NotNull(message = "商品id不能为空")
	private Long goodsId;
	/**
	 * 商品规格id
	 */
	@NotNull(message = "商品规格id不能为空")
	private Long goodsSpecsId;
	/**
	 * 所属机构
	 */
	@NotNull(message = "所属机构id不能为空")
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
	 * 是否上传(0:未上传;1:已上传)
	 */
	private Integer isUpload;

	/**
	 *平台商品id
	 */
	private Integer platformGoodsId;

	/**
	 *平台商品编码
	 */
	private String platformGoodsCode;

	/**
	 *平台商品规格编码
	 */
	private String platformGoodsSpecsCode;

	/**
	 *平台商品规格id
	 */
	private String platformGoodsSpecsId;

}
