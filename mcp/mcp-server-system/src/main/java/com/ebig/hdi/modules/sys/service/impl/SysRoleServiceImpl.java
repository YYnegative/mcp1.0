/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.sys.dao.SysRoleDao;
import com.ebig.hdi.modules.sys.entity.SysDeptEntity;
import com.ebig.hdi.modules.sys.entity.SysRoleEntity;
import com.ebig.hdi.modules.sys.service.SysDeptService;
import com.ebig.hdi.modules.sys.service.SysRoleDeptService;
import com.ebig.hdi.modules.sys.service.SysRoleMenuService;
import com.ebig.hdi.modules.sys.service.SysRoleService;
import com.ebig.hdi.modules.sys.service.SysUserRoleService;

/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	public List<SysRoleEntity> selectListByUserId(Long userId) {
		return this.baseMapper.selectListByUserId(userId);
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String) params.get("roleName");

		Page<SysRoleEntity> page = this.selectPage(new Query<SysRoleEntity>(params).getPage(),
				new EntityWrapper<SysRoleEntity>().like(StringUtils.isNotBlank(roleName), "role_name", roleName)
						.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null,
								(String) params.get(Constant.SQL_FILTER)));

		for (SysRoleEntity sysRoleEntity : page.getRecords()) {
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysRoleEntity.getDeptId());
			if (sysDeptEntity != null) {
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
			sysRoleEntity.setMenuIdList(sysRoleMenuService.queryMenuIdList(sysRoleEntity.getRoleId()));
			sysRoleEntity.setDeptIdList(sysRoleDeptService.queryDeptIdList(new Long[] { sysRoleEntity.getRoleId() }));
		}

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysRoleEntity role) {
		role.setCreateTime(new Date());
		this.insert(role);

		// 保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		// 保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
		this.updateById(role);

		// 更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		// 保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		// 删除角色
		this.deleteBatchIds(Arrays.asList(roleIds));

		// 删除角色与菜单关联
		sysRoleMenuService.deleteBatch(roleIds);

		// 删除角色与部门关联
		sysRoleDeptService.deleteBatch(roleIds);

		// 删除角色与用户关联
		sysUserRoleService.deleteBatch(roleIds);
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public List<SysRoleEntity> selectAll(Map<String, Object> params) {
		List<SysRoleEntity> list = this.baseMapper.selectList(new EntityWrapper<SysRoleEntity>()
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));
		return list;
	}
}
