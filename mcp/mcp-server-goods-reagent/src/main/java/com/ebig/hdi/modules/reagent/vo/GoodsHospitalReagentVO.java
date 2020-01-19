package com.ebig.hdi.modules.reagent.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentSpecsEntity;

import lombok.Data;

/**
 * 医院试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:30:58
 */
@Data
public class GoodsHospitalReagentVO extends GoodsHospitalReagentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 厂商名称
	 */
	private String factoryName;
	
	/**
	 * 医院名称
	 */
	private String hospitalName;
	
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
	 * 医院试剂规格list
	 */
	private List<GoodsHospitalReagentSpecsEntity> specsEntityList;
}
