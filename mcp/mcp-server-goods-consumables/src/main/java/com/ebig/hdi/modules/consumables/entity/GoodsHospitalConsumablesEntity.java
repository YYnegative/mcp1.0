package com.ebig.hdi.modules.consumables.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;

import lombok.Data;

/**
 * 医院耗材信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:01
 */
@Data
@TableName("hdi_goods_hospital_consumables")
public class GoodsHospitalConsumablesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 医院id
	 */
	@NotNull(message="医院id不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Long hospitalId;
	/**
	 * 耗材编码
	 */
	private String consumablesCode;
	/**
	 * 耗材名称
	 */
	@NotBlank(message="商品名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String consumablesName;
	/**
	 * 通用名称
	 */
	private String commonName;
	/**
	 * 商品属性(0:国产;1:进口)
	 */
	@NotNull(message="请选择商品属性", groups = {AddGroup.class, UpdateGroup.class})
	private Integer goodsNature;
	/**
	 * 商品分类id
	 */
	private String typeId;
	/**
	 * 分类名称
	 */
	@NotBlank(message="商品分类名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String typeName;
	/**
	 * 生产厂商id
	 */
	private String factoryId;
	/**
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态", groups = {AddGroup.class, UpdateGroup.class})
	private Integer status;
	/**
	 * 商品单位
	 */
	private String goodsUnit;
	/**
	 * 采购单位
	 */
	private String buyingUnit;
	/**
	 * 转换单位
	 */
	private String convertUnit;
	/**
	 * 图片地址
	 */
	private String picUrl;
	/**
	 * 所属机构
	 */
	private Long deptId;
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
	 * 是否删除(-1:已删除;0:正常)
	 */
	@TableLogic
	private Integer delFlag;
	/**
	 * 是否匹对(0:未匹对;1:已匹对)
	 */
	private Integer isMatch;
	/**
	 * 数据来源(0:系统录入;1:医院SPD)
	 */
	private Integer dataSource;

}
