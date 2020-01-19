package com.ebig.hdi.modules.reagent.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableField;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;

import lombok.Data;

/**
 * 供应商试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
@Data
public class GoodsSupplierReagentVO extends GoodsSupplierReagentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 厂商名称
	 */
	private String factoryName;
	
	/**
	 * 供应商名称
	 */
	private String supplierName;
	
	/**
	 * 代理商名称
	 */
	private String agentName;
	
	/**
	 * 全球唯一码
	 */
	private String guid;
	
	/**
	 * 商品规格
	 */
	@NotBlank(message="商品规格不能为空", groups = {AddGroup.class})
	private String specs;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	@TableField(exist=false)
	private String fileterDept;
	
	/**
	 * 供应商试剂规格list
	 */
	private List<GoodsSupplierReagentSpecsEntity> specsEntityList;
	
	/**
	 * 证照数量
	 */
	private Integer licenseNumber; 
}
