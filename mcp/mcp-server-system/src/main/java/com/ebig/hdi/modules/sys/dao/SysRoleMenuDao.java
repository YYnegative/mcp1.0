/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.sys.entity.SysRoleMenuEntity;

/**
 * 角色与菜单对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> queryMenuIdList(Long roleId);
	
	/**
	 * 根据角色ID，获取菜单ID列表
	 */
	List<Long> queryMenuIdListByArr(Long[] roleIdArr);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);

	List<SysRoleMenuEntity> selectListByRoleId(@Param("roleId") String roleId);
}
