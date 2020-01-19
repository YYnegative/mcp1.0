/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
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
import com.ebig.hdi.common.validator.Assert;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AddGroup;
import com.ebig.hdi.common.validator.group.UpdateGroup;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import com.ebig.hdi.modules.sys.service.SysDeptService;
import com.ebig.hdi.modules.sys.service.SysUserRoleService;
import com.ebig.hdi.modules.sys.service.SysUserService;
import com.ebig.hdi.modules.sys.shiro.ShiroUtils;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public Hdi list(@RequestParam Map<String, Object> params){
		PageUtils page = sysUserService.queryPage(params);

		return Hdi.ok().put("page", page);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public Hdi info(){
		SysUserEntity user = getUser();
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
		Map<String,Object> map = sysUserService.selectSupplierIdAndNameByDeptId(user.getDeptId());
		if(map != null && map.get("id") != null){
			user.setSupplierId(Long.valueOf(map.get("id").toString()));
			user.setSupplierName((String)map.get("supplier_name"));
		}
		user.setRoleIdList(roleIdList);
		user.setDeptName(sysDeptService.selectById(user.getDeptId()).getName());
		return Hdi.ok().put("user", user);
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
	public Hdi password(String password, String newPassword){
		Assert.isBlank(newPassword, "新密码不为能空");

		//原密码
		password = ShiroUtils.sha256(password, getUser().getSalt());
		//新密码
		newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return Hdi.error("原密码不正确");
		}
		
		return Hdi.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public Hdi info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.selectById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		user.setDeptName(sysDeptService.selectById(user.getDeptId()).getName());
		
		return Hdi.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public Hdi save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		sysUserService.save(user);
		
		return Hdi.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public Hdi update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		sysUserService.update(user);
		
		return Hdi.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public Hdi delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return Hdi.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return Hdi.error("当前用户不能删除");
		}

		sysUserService.deleteBatchIds(Arrays.asList(userIds));
		
		return Hdi.ok();
	}
}
