package com.ebig.hdi.modules.reagent.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentApprovalEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentSpecsEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 医院试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:30:58
 */
@Data
public class GoodsPlatformReagentVO extends GoodsPlatformReagentApprovalEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String factoryName;
	
	/**
	 * 商品分类名称
	 */
	private String typeName;
	
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
	 * 平台试剂规格列表
	 */
	private List<GoodsPlatformReagentSpecsEntity> specsEntityList;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	@TableField(exist=false)
	private String fileterDept;
	
}
