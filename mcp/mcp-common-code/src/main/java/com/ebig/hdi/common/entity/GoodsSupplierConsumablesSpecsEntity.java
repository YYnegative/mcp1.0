package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商耗材规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
@Data
@TableName("hdi_goods_supplier_consumables_specs")
public class GoodsSupplierConsumablesSpecsEntity implements Serializable {
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
	 * 耗材id
	 */
	private Long consumablesId;
	/**
	 * 规格编码
	 */
	private String specsCode;
	/**
	 * 商品规格
	 */
	@NotBlank(message="商品规格不能为空")
	private String specs;
	/**
	 * 全球唯一码
	 */
	private String guid;
	/**
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态")
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
		if (obj instanceof GoodsSupplierConsumablesSpecsEntity) {
			GoodsSupplierConsumablesSpecsEntity entity= (GoodsSupplierConsumablesSpecsEntity) obj;
			if(this.getConsumablesId().equals(entity.getConsumablesId()) && this.getSpecs().equals(entity.getSpecs())){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		int result = this.getConsumablesId().hashCode();
		result = 19 * result + this.getSpecs().hashCode();
		return result;

	}
}
