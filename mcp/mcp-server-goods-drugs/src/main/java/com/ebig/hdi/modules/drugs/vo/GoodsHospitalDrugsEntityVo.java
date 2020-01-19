package com.ebig.hdi.modules.drugs.vo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;

import lombok.Data;

/**
 * 医院药品信息包装类
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-05-15 02:14:53
 */
@Data
public class GoodsHospitalDrugsEntityVo extends GoodsHospitalDrugsEntity implements Serializable{ 
	
	private static final long serialVersionUID = 1L;

	/**
	 * 医院药品生产厂商名称
	 */
	private String factoryName;
	
	/**
	 * 医院名称
	 */
	private String hospitalName;
	
	/**
	 * 医院药品规格
	 */
	@NotBlank(message="商品规格不能为空", groups = {AddGroup.class})
	private String specs;
	
	/**
	 * 全球唯一码
	 */
	private String guid;
	
	/**
	 * 医院药品规格列表
	 */
	private List<GoodsHospitalDrugsSpecsEntity> specsEntityList;

}
