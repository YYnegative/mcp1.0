/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 保存用户
	 * @param user 新增的用户
	 * @param userType 用户类型
	 */
	void save(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	
	/**
	 * 通过用户名查询供应商id
	 * @param userName
	 * @return
	 */
	Map<String,Object> selectSupplierIdAndNameByUserName(String userName);

	/**
	 * 根据部门ID查询供应商
	 * @param deptId
	 * @return
	 */
	Map<String, Object> selectSupplierIdAndNameByDeptId(Long deptId);
}
