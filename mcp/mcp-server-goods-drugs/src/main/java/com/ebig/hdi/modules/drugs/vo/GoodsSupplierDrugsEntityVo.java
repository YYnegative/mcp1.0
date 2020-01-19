package com.ebig.hdi.modules.drugs.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商药品信息包装类
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-05-13 10:23:30
 */
@Data
public class GoodsSupplierDrugsEntityVo extends GoodsSupplierDrugsEntity implements Serializable{ 
	
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商名称
	 */
	private String supplierName;
	
	/**
	 * 供应商药品生产厂商名称
	 */
	private String factoryName;
	
	/**
	 * 代理商名称
	 */
	private String agentName;
	
	/**
	 * 供应商药品规格
	 */
	@NotBlank(message="商品规格不能为空", groups= {AddGroup.class})
	private String specs;
	
	/**
	 * 全球唯一码
	 */
	private String guid;
	
	/**
	 * 供应商药品规格列表
	 */
	private List<GoodsSupplierDrugsSpecsEntity> specsEntityList;
	
	/**
	 * 已关联证照数量
	 */
	private Integer licenseNumber;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (!(o instanceof GoodsSupplierDrugsEntityVo)) {return false;}
		GoodsSupplierDrugsEntityVo that = (GoodsSupplierDrugsEntityVo) o;
		return Objects.equals(getDrugsName(), that.getDrugsName()) &&
				Objects.equals(getApprovals(), that.getApprovals())&&Objects.equals(getSpecs(), that.getSpecs());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDrugsName(), getApprovals(),getSpecs());
	}
}
