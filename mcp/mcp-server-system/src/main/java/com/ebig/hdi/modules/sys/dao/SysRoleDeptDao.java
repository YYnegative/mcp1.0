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

package com.ebig.hdi.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.sys.entity.SysRoleDeptEntity;

/**
 * 角色与部门对应关系
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017年6月21日 23:33:46
 */
public interface SysRoleDeptDao extends BaseMapper<SysRoleDeptEntity> {
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<Long> queryDeptIdList(Long[] roleIds);
	
	/**
	 * 根据部门ID，获取角色ID列表
	 */
	List<Long> queryRoleIdList(@Param("deptId") Long deptId) ;

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
	
	List<SysRoleDeptEntity> selectByRoleIdAndDeptId(@Param("roleId") Long roleId,@Param("deptId") Long deptId);
	
	/**
	 * 根据角色id和部门id删除角色部门中间表的数据
	 * @param roleId
	 * @param deptId
	 */
	void deleteByRoleIdAndDeptId(@Param("roleId") Long roleId,@Param("deptId") Long deptId);
}
