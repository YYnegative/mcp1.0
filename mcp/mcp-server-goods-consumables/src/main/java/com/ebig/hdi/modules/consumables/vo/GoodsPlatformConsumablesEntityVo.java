package com.ebig.hdi.modules.consumables.vo;

import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesSpecsEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 平台耗材包装类
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-05-17 10:03:31
 */
@Data
public class GoodsPlatformConsumablesEntityVo extends GoodsPlatformConsumablesApprovalEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 平台耗材生产厂商名称
	 */
	private String factoryName;
	
	/**
	 * 商品分类名称
	 */
	private String typeName;
	
	/**
	 * 平台耗材规格
	 */
	@NotBlank(message="商品规格不能为空", groups = {AddGroup.class})
	private String specs;

	/**
	 * 全球唯一码
	 */
	private String guid;
	
	/**
	 * 平台耗材批准文号数组
	 */
	@NotNull(message="批准文号不能为空", groups = {AddGroup.class})
	private String[] approvals;
	
	/**
	 * 平台耗材规格列表
	 */
	private List<GoodsPlatformConsumablesSpecsEntity> specsEntityList;
	
	/**
	 * 平台耗材批准文号列表
	 */
	private List<GoodsPlatformConsumablesApprovalsEntity> approvalsEntityList;

}
