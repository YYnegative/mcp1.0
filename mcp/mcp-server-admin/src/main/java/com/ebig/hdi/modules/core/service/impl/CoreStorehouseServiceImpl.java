package com.ebig.hdi.modules.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.core.dao.CoreStorehouseDao;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;
import com.ebig.hdi.modules.core.service.CoreStorehouseService;


@Service("coreStorehouseService")
public class CoreStorehouseServiceImpl extends ServiceImpl<CoreStorehouseDao, CoreStorehouseEntity> implements CoreStorehouseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CoreStorehouseEntity> page = this.selectPage(
                new Query<CoreStorehouseEntity>(params).getPage(),
                new EntityWrapper<CoreStorehouseEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<CoreStorehouseEntity> queryAllByHospitalId(Long hospitalId) {
		if(hospitalId == null){
			throw new HdiException("传入的医院id不能为空");
		}
		return baseMapper.queryAllByHospitalId(hospitalId);
	}

	
	@Override
	public CoreStorehouseEntity selectSupplyAddr(Long storehouseid) {
		return this.baseMapper.selectSupplyAddr(storehouseid);
	}
	
	
	//HDI转换用  查询是否存在此原始标识对应的库房
	@Override
	public CoreStorehouseEntity selectByOrgdataid(String orgdataid) {
		return this.baseMapper.selectByOrgdataid(orgdataid);
	}

	@Override
	public List<CoreStorehouseEntity> selectByOrgdataids(List<String> orgdataids) {
		return this.baseMapper.selectByOrgdataids(orgdataids);
	}

	@Override
	public List<CoreStorehouseEntity> selectByStorehouseName(String storehouseName) {
		return this.baseMapper.selectByStoreName(storehouseName);
	}


}
