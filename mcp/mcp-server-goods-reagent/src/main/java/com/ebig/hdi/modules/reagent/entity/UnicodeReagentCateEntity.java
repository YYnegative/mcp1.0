package com.ebig.hdi.modules.reagent.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-16 09:31:59
 */
@Data
@TableName("hdi_unicode_reagent_cate")
public class UnicodeReagentCateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品分类标识
	 */
	@TableId
	private Long cateId;
	/**
	 * 分类名称
	 */
	private String cateName;
	/**
	 * 分类编码
	 */
	private String cateNo;
	/**
	 * 层级顺序
	 */
	private Long levelOrder;
	/**
	 * 根节点为1，第一层，依次往下
	 */
	private Integer cateLevel;
	/**
	 * 父分类标识
	 */
	private Long pcateId;
	/**
	 * 机构标识
	 */
	private Long deptId;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 是否删除(-1:已删除;0:正常)
	 */
	@TableLogic
	private Integer delFlag;

}
