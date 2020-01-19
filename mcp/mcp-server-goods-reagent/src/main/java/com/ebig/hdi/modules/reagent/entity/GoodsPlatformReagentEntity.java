package com.ebig.hdi.modules.reagent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;

import lombok.Data;

/**
 * 平台试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
@Data
@TableName("hdi_goods_platform_reagent")
public class GoodsPlatformReagentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.INPUT)
	private Long id;
	/**
	 * 商品统一编码
	 */
	private String goodsUnicode;
	/**
	 * 试剂编码
	 */
	private String reagentCode;
	/**
	 * 试剂名称
	 */
	@NotBlank(message="商品名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String reagentName;
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
	@NotNull(message="请选择商品分类", groups = {AddGroup.class, UpdateGroup.class})
	private Long typeId;
	/**
	 * 生产厂商id
	 */
	@NotBlank(message="本生产厂商未建档，请先建档", groups = {AddGroup.class, UpdateGroup.class})
	private String factoryId;
	/**
	 * 批准文号
	 */
	@NotBlank(message="批准文号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String approvals;
	/**
	 * 状态(0:停用;1:启用)
	 */
	@NotNull(message="请选择状态", groups = {AddGroup.class, UpdateGroup.class})
	private Integer status;
	/**
	 * 商品单位
	 */
	@NotBlank(message="请选择商品单位", groups = {AddGroup.class, UpdateGroup.class})
	private String goodsUnit;
	/**
	 * 储存方式
	 */
	private String storeWay;
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

}
