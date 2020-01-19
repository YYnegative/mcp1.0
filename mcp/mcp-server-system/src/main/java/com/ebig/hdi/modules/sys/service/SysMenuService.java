/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.sys.entity.SysMenuEntity;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysMenuService extends IService<SysMenuEntity> {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);
	
	/**
	 * 获取所有菜单，形成树结构
	 * @return
	 */
	List<SysMenuEntity> tree();

	/**
	 * 删除
	 */
	void delete(Long menuId);

	/**
	 * 获取该用户的所有菜单，形成树结构
	 * @param userId
	 * @return
	 */
	List<SysMenuEntity> tree(Long userId);
}
