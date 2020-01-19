package com.ebig.hdi.modules.consumables.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 平台耗材批准文号
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
@Data
@TableName("hdi_goods_platform_consumables_approvals")
public class GoodsPlatformConsumablesApprovalsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 耗材id
	 */
	private Long consumablesId;
	/**
	 * 批准文号
	 */
	private String approvals;
	/**
	 * 状态(0:停用;1:启用)
	 */
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

}
