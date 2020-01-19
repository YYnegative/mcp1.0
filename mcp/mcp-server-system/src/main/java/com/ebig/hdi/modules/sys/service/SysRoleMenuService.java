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
import com.ebig.hdi.modules.sys.entity.SysRoleMenuEntity;

/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {
	
	void saveOrUpdate(Long roleId, List<Long> menuIdList);
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> queryMenuIdList(Long roleId);
	
	/**
	 * 根据角色ID数组，获取菜单ID列表
	 */
	List<Long> queryMenuIdList(Long[] roleIds);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);

	List<SysRoleMenuEntity> selectListByRoleId(String roleId);
	
}
