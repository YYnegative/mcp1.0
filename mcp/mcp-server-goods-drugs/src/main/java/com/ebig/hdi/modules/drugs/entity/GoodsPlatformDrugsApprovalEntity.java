package com.ebig.hdi.modules.drugs.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 平台药品信息待审批表
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-22 10:25:17
 */
@TableName("hdi_goods_platform_drugs_approval")
@Data
public class GoodsPlatformDrugsApprovalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 商品统一编码
	 */
	private String goodsUnicode;
	/**
	 * 药品编码
	 */
	private String drugsCode;
	/**
	 * 药品名称
	 */
	private String drugsName;
	/**
	 * 通用名称
	 */
	private String commonName;
	/**
	 * 商品属性(0:国产;1:进口)
	 */
	private Integer goodsNature;
	/**
	 * 商品分类id
	 */
	private Long typeId;
	/**
	 * 生产厂商id
	 */
	private String factoryId;
	/**
	 * 批准文号
	 */
	private String approvals;
	/**
	 * 状态(0:停用;1:启用)
	 */
	private Integer status;
	/**
	 * 商品单位
	 */
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
	private Integer delFlag;
	/**
	 * 审批状态(0.待审批 1.审批通过 2.审批不通过)
	 */
	private Integer checkStatus;
	/**
	 * 流程实例id
	 */
	private String processId;

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (!(o instanceof GoodsPlatformDrugsApprovalEntity)) {return false;}
		GoodsPlatformDrugsApprovalEntity that = (GoodsPlatformDrugsApprovalEntity) o;
		return Objects.equals(getDrugsName(), that.getDrugsName()) &&
				Objects.equals(getApprovals(), that.getApprovals());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDrugsName(), getApprovals());
	}
}
