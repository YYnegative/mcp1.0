package com.ebig.hdi.modules.consumables.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商耗材信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
@Data
@TableName("hdi_goods_supplier_consumables")
public class GoodsSupplierConsumablesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 供应商id
	 */
	@NotNull(message="供应商id不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Long supplierId;
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
	@TableField(strategy = FieldStrategy.IGNORED)
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
	@NotBlank
	private String goodsUnit;
	/**
	 * 供货单位
	 */
	private String supplyUnit;
	/**
	 * 转换单位
	 */
	private String convertUnit;
	/**
	 * 代理商id
	 */
	private Long agentId;
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
	 * 数据来源(0:系统录入;1:供应商ERP)
	 */
	private Integer dataSource;
	/**
	 * 是否上传(0:未上传;1:已上传)
	 */
	private Integer isUpload;
	

	/**
	 * 储存方式
	 */
	private String storeWay;


	/**
	 * 阳光采购平台编码
	 */
	private String sunshinePno;
}
