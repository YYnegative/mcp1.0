/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	
	/**
	 * 通过用户名查询供应商id
	 * @param userName
	 * @return
	 */
	Map<String,Object> selectSupplierIdAndNameByUserName(@Param("userName") String userName);

	/**
	 * 根据部门ID查询供应商
	 * @param deptId
	 * @return
	 */
	Map<String, Object> selectSupplierIdAndNameByDeptId(@Param("deptId") Long deptId);
}
