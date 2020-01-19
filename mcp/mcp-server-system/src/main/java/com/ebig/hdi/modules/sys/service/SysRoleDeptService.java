/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ebig.hdi.modules.sys.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.sys.entity.SysRoleDeptEntity;


/**
 * 角色与部门对应关系
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017年6月21日 23:42:30
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity> {
	
	void saveOrUpdate(Long roleId, List<Long> deptIdList);
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<Long> queryDeptIdList(Long[] roleIds) ;
	
	/**
	 * 根据部门ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(Long deptId) ;
	
	/**
	 * 根据角色ID和部门ID，获取所有角色部门中间表数据
	 */
	List<SysRoleDeptEntity> selectByRoleIdAndDeptId(Long roleId,Long deptId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
	
	/**
	 * 根据角色id和部门id删除角色部门中间表的数据
	 * @param roleId
	 * @param deptId
	 */
	void deleteByRoleIdAndDeptId(Long roleId,Long deptId);
}
