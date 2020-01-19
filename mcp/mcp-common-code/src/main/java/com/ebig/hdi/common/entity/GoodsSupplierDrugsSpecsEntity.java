package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商药品规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:17:50
 */
@Data
@TableName("hdi_goods_supplier_drugs_specs")
public class GoodsSupplierDrugsSpecsEntity implements Serializable {
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
	 * 药品id
	 */
	private Long drugsId;
	/**
	 * 规格编码
	 */
	private String specsCode;
	/**
	 * 商品规格
	 */
	@NotBlank(message="商品规格不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String specs;
	/**
	 * 全球唯一码
	 */
	private String guid;
	/**
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态", groups = {AddGroup.class, UpdateGroup.class})
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


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GoodsSupplierDrugsSpecsEntity) {
			GoodsSupplierDrugsSpecsEntity entity= (GoodsSupplierDrugsSpecsEntity) obj;
			if(this.getDrugsId().equals(entity.getDrugsId()) && this.getSpecs().equals(entity.getSpecs())){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		int result = this.getDrugsId().hashCode();
		result = 19 * result + this.getSpecs().hashCode();
		return result;

	}
}
