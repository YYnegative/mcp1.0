/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.annotation.SysLog;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.sys.entity.SysRoleEntity;
import com.ebig.hdi.modules.sys.service.SysDeptService;
import com.ebig.hdi.modules.sys.service.SysRoleDeptService;
import com.ebig.hdi.modules.sys.service.SysRoleMenuService;
import com.ebig.hdi.modules.sys.service.SysRoleService;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysDeptService sysDeptService;
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public Hdi list(@RequestParam Map<String, Object> params){
		PageUtils page = sysRoleService.queryPage(params);

		return Hdi.ok().put("page", page);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public Hdi select(){
		List<SysRoleEntity> list = null;
		//超级管理员列出所有角色，非超级管理员列出已分配的角色
		if(getUserId() == 1L){
			list = sysRoleService.selectList(null);
		}else{
			list = sysRoleService.selectListByUserId(getUserId());
		}
		setOtherValues(list);
		return Hdi.ok().put("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public Hdi info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.selectById(roleId);
		
		role.setMenuIdList(sysRoleMenuService.queryMenuIdList(roleId));
		role.setDeptIdList(sysRoleDeptService.queryDeptIdList(new Long[]{roleId}));
		role.setDeptName(sysDeptService.selectById(role.getDeptId()).getName());
		
		return Hdi.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public Hdi save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		sysRoleService.save(role);
		
		return Hdi.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public Hdi update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		sysRoleService.update(role);
		
		return Hdi.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public Hdi delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		
		return Hdi.ok();
	}
	
	/**
	 * 设置其他属性（部门名称、菜单ID列表 、部门ID列表）
	 * @param list
	 */
	private void setOtherValues(List<SysRoleEntity> list){
		if(list != null){
			for(SysRoleEntity entity : list){
				entity.setDeptName(sysDeptService.selectById(entity.getDeptId()).getName());
				entity.setMenuIdList(sysRoleMenuService.queryMenuIdList(entity.getRoleId()));
				entity.setDeptIdList(sysRoleDeptService.queryDeptIdList(new Long[]{entity.getRoleId()}));
			}
		}
	}
	
	/**
	 * 查询全部角色列表
	 */
	@RequestMapping("/selectAll")
	@RequiresPermissions("sys:role:selectAll")
	public Hdi selectAll(){
		List<SysRoleEntity> list = sysRoleService.selectAll(new HashMap());
		return Hdi.ok().put("list", list);
	}
}
