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

package com.ebig.hdi.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.modules.sys.dao.SysDeptDao;
import com.ebig.hdi.modules.sys.entity.SysDeptEntity;
import com.ebig.hdi.modules.sys.service.SysDeptService;

@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
	
	@Override
	@DataFilter(subDept = true, user = false)
	public List<SysDeptEntity> queryList(Map<String, Object> params){
		List<SysDeptEntity> deptList =
			this.selectList(new EntityWrapper<SysDeptEntity>()
			.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER)));

		for(SysDeptEntity sysDeptEntity : deptList){
			SysDeptEntity parentDeptEntity =  this.selectById(sysDeptEntity.getParentId());
			if(parentDeptEntity != null){
				sysDeptEntity.setParentName(parentDeptEntity.getName());
			}
		}
		return deptList;
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public List<Long> getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();
		deptIdList.add(deptId);

		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		return deptIdList;
	}

	/**
	 * 递归获取所有子部门ID
	 * @param subIdList 子部门ID列表
	 * @param deptIdList 用于获取子孙部门ID的列表
	 */
	private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}
	
	@Override
	@DataFilter(subDept = true, user = false)
	public List<SysDeptEntity> queryTree(Map<String, Object> params){
		//查询所有父部门
		List<SysDeptEntity> deptList =
			this.selectList(new EntityWrapper<SysDeptEntity>()
			.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
			.eq("parent_id", 0));

		
		for(SysDeptEntity dept : deptList){
			findAllSubDept(dept, null);
		}
		return deptList;
	}
	
	public List<SysDeptEntity> selectSubDeptByDeptId(Long deptId){
		return this.baseMapper.selectSubDeptByDeptId(deptId);
	}
	
	/**
	 * 递归获取部门树
	 * @param dept 根节点部门
	 * @param deptList 用于获取子孙节点部门的列表
	 */
	private void findAllSubDept(SysDeptEntity dept, List<SysDeptEntity> deptList){
			List<SysDeptEntity> subDeptList = selectSubDeptByDeptId(dept.getDeptId());
			if(subDeptList != null) {
				if(subDeptList.size() == 0){
					dept.setOpen(false);
				}
				for(SysDeptEntity subDept : subDeptList){
					if(deptList != null){
						deptList.add(subDept);
					}
					List<SysDeptEntity> list = dept.getList();
					if(list == null){
						list = new ArrayList<>();
					}
					list.add(subDept);
					dept.setList(list);
					dept.setOpen(true);
					findAllSubDept(subDept,deptList);
				}
			}
	}
}
