package com.ebig.hdi.modules.drugs.vo;

import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsApprovalEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsSpecsEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 平台药品信息包装类
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-05-10 09:38:14
 */
@Data
public class GoodsPlatformDrugsEntityVo extends GoodsPlatformDrugsApprovalEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 平台药品生产厂商名称
	 */
	private String factoryName;
	
	/**
	 * 商品分类名称
	 */
	private String typeName;
	
	/**
	 * 平台药品规格
	 */
	@NotBlank(message="商品规格不能为空", groups = {AddGroup.class})
	private String specs;
	
	/**
	 * 全球唯一码
	 */
	private String guid;
	
	/**
	 * 平台药品规格列表
	 */
	private List<GoodsPlatformDrugsSpecsEntity> specsEntityList;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (!(o instanceof GoodsPlatformDrugsEntityVo)) {return false;}
		GoodsPlatformDrugsEntityVo that = (GoodsPlatformDrugsEntityVo) o;
		return Objects.equals(getDrugsName(), that.getDrugsName()) &&
				Objects.equals(getApprovals(), that.getApprovals())&&Objects.equals(getSpecs(),that.getSpecs());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDrugsName(), getApprovals(),getSpecs());
	}

}
