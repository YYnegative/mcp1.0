package com.ebig.hdi.modules.consumables.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesSpecsEntity;

import lombok.Data;

/**
 * 医院耗材包装类
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-05-17 10:03:20
 */
@Data
public class GoodsHospitalConsumablesEntityVo extends GoodsHospitalConsumablesEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 医院耗材生产厂商名称
	 */
	private String factoryName;
	
	/**
	 * 医院名称
	 */
	private String hospitalName;
	
	/**
	 * 医院耗材规格
	 */
	@NotBlank(message="商品规格不能为空", groups = {AddGroup.class})
	private String specs;
	
	/**
	 * 全球唯一码
	 */
	private String guid;
	
	/**
	 * 医院耗材批准文号数组
	 */
	@NotNull(message="批准文号不能为空", groups = {AddGroup.class})
	private String[] approvals;
	
	/**
	 * 医院耗材规格列表
	 */
	private List<GoodsHospitalConsumablesSpecsEntity> specsEntityList;
	
	/**
	 * 医院耗材批准文号列表
	 */
	private List<GoodsHospitalConsumablesApprovalsEntity> approvalsEntityList;

}
