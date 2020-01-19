package com.ebig.hdi.modules.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.enums.DelFlagEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.dao.CoreAcceptMasterDao;
import com.ebig.hdi.modules.core.entity.CoreAcceptMasterEntity;
import com.ebig.hdi.modules.core.service.CoreAcceptMasterService;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;


@Service("coreAcceptMasterService")
public class CoreAcceptMasterServiceImpl extends ServiceImpl<CoreAcceptMasterDao, CoreAcceptMasterEntity> implements CoreAcceptMasterService {

	
	
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "cam")
    public PageUtils queryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		
		// 手动数据过滤
		if (params.get(Constant.SQL_FILTER) != null) {
			String sqlFilter = params.get(Constant.SQL_FILTER).toString();
			params.put("deptIds", sqlFilter);
		}

		Page<CoreAcceptMasterEntity> page = new Page<CoreAcceptMasterEntity>(currPage, pageSize);
		List<CoreAcceptMasterEntity> list = this.baseMapper.selectByDeptId(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }
    
    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "cam")
    public PageUtils bedingungenQueryPage(Map<String, Object> params) {
		int currPage = Integer.parseInt(params.get("page").toString());
		int pageSize = Integer.parseInt(params.get("limit").toString());
		// 手动数据过滤
		if (params.get(Constant.SQL_FILTER) != null) {
			String sqlFilter = params.get(Constant.SQL_FILTER).toString();
			params.put("deptIds", sqlFilter);
		}

		Page<CoreAcceptMasterEntity> page = new Page<CoreAcceptMasterEntity>(currPage, pageSize);
		List<CoreAcceptMasterEntity> list = this.baseMapper.selectByBedingungen(page, params);
		page.setRecords(list);
		return new PageUtils(page);
    }
    
    
    @Override
    public void deleteMaster(List<CoreAcceptMasterEntity> listEntity,SysUserEntity user) {
    	if (user.getUserType()!=1) {
    		throw new HdiException("当前登录用户无此权限");
		}
    	for (CoreAcceptMasterEntity coreAcceptMasterEntity : listEntity) {
    		coreAcceptMasterEntity.setDelFlag(DelFlagEnum.DELETE.getKey());
    		this.baseMapper.updateById(coreAcceptMasterEntity);
		}
    }
    
    
    public CoreAcceptMasterEntity selectAcceptMaster(Long deptId,Long horgId,Long storehouseid) {
    	CoreAcceptMasterEntity acceptMaster = this.baseMapper.selectAcceptMaster(deptId, storehouseid, horgId);
    	return acceptMaster;
    }

}
