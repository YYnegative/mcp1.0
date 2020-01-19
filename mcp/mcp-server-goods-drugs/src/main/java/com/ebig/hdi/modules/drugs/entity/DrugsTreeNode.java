package com.ebig.hdi.modules.drugs.entity;

import java.util.List;

import lombok.Data;

/**
 * 分类树结构
 * @author frink
 *
 */
@Data
public class DrugsTreeNode{


	/**
	 * 分类id
	 */
	private Long cateId;

	/**
	 * 叶子结点标识
	 */
	private boolean leaf = false;

	/**
	 * 分类名称
	 */
	private String cateName;

	/**
	 * 层级顺序
	 */
	private Long levelOrder;
	
	/**
	 * 分类层级
	 */
	private Integer cateLevel;
	
	/**
	 * 父分类id
	 */
	private Long pcateId;
	
	/**
	 * 机构标识
	 */
	private Long deptId;
	
	/**
	 * 分类编码
	 */
	private String cateNo;
	/**
	 * 父类别名称
	 */
	private String pcateName;
	
	/**
	 * 子节点
	 */
	private List<DrugsTreeNode> children;
}
