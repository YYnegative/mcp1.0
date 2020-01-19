package com.ebig.hdi.modules.reagent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 医院试剂规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
@Data
@TableName("hdi_goods_hospital_reagent_specs")
public class GoodsHospitalReagentSpecsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 原系统商品规格id
	 */
	private String sourcesSpecsId;
	/**
	 * 试剂id
	 */
	private Long reagenId;
	/**
	 * 规格编码
	 */
	private String specsCode;
	/**
	 * 商品规格
	 */
	@NotBlank(message="商品规格不能为空")
	private String specs;
	/**
	 * 全球唯一码
	 */
	private String guid;
	/**
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态")
	private Integer status;
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
	 * 是否引用(0:否;1:是)
	 */
	@TableField(exist = false)
	private Integer isCite;

}
