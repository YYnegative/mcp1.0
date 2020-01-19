/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.MapUtils;
import com.ebig.hdi.modules.sys.dao.SysMenuDao;
import com.ebig.hdi.modules.sys.entity.SysMenuEntity;
import com.ebig.hdi.modules.sys.service.SysMenuService;
import com.ebig.hdi.modules.sys.service.SysRoleMenuService;
import com.ebig.hdi.modules.sys.service.SysUserService;

@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		List<SysMenuEntity> menuList = baseMapper.queryNotButtonList();
		if(menuList != null){
			for(SysMenuEntity menu : menuList){
				SysMenuEntity parentMenu = this.selectById(menu.getParentId());
				menu.setParentName(parentMenu != null?parentMenu.getName():null);
			}
		}
		return menuList;
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.deleteById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.deleteByMap(new MapUtils().put("menu_id", menuId));
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		
		for(SysMenuEntity entity : menuList){
			SysMenuEntity parentMenu = this.selectById(entity.getParentId());
			if(parentMenu != null){
				entity.setParentName(parentMenu.getName());
			}
			
			//若菜单类型为目录或菜单，则设置子元素
			if(entity.getType().equals(Constant.MenuType.CATALOG.getValue()) || entity.getType().equals(Constant.MenuType.MENU.getValue()))
			entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}

	@Override
	public List<SysMenuEntity> tree() {
		//先查询出所有父id为0的顶级菜单
		List<SysMenuEntity> parentList = baseMapper.queryListParentId(0L);
		//递归查询出所有的子菜单
		setAllChildMenu(parentList);
		return parentList;
	}
	
	/**
	 * 递归查询出所有的子菜单
	 * @param parentList
	 */
	public void setAllChildMenu(List<SysMenuEntity> parentList){
		if( parentList!=null ){
			for(SysMenuEntity entity : parentList){
				List<SysMenuEntity> childList = baseMapper.queryListParentId(entity.getMenuId());
				entity.setList(childList);
				setAllChildMenu(childList);
			}
		}
	}

	@Override
	public List<SysMenuEntity> tree(Long userId) {
		//先查询出该用户所有父id为0的顶级菜单
		List<SysMenuEntity> parentList = baseMapper.selectListByParentIdAndUserId(0L, userId);
		//递归查询出该用户所有的子菜单
		setAllChildMenu(parentList, userId);
		return parentList;
	}
	
	/**
	 * 递归查询出所有的子菜单
	 * @param parentList
	 */
	private void setAllChildMenu(List<SysMenuEntity> parentList, Long userId){
		if( parentList!=null ){
			for(SysMenuEntity entity : parentList){
				List<SysMenuEntity> childList = baseMapper.selectListByParentIdAndUserId(entity.getMenuId(), userId);
				entity.setList(childList);
				setAllChildMenu(childList, userId);
			}
		}
	}
}
