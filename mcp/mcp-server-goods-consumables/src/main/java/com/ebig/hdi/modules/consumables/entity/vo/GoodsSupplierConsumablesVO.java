package com.ebig.hdi.modules.consumables.entity.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;

import lombok.Data;

/**
 * 供应商耗材信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
@Data
public class GoodsSupplierConsumablesVO extends GoodsSupplierConsumablesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 供应商名称
	 */
	private String supplierName;
	
	/**
	 * 代理商名称
	 */
	private String agentName;
	
	/**
	 * 厂商名称
	 */
	private String factoryName;
	
	/**
	 * 批文名称list
	 */
	@NotNull(message="批准文号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String approvals;
	
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
	private String fileterDept;
	
	/**
	 * 批准文号List
	 */
	//private List<GoodsSupplierConsumablesApprovalsEntity> approvalsEntityList;
	
	/**
	 * 规格list
	 */
	private List<GoodsSupplierConsumablesSpecsEntity> specsEntityList;
	
	private Integer licenseNumber;
}
