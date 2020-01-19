package com.ebig.hdi.modules.core.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;

/**
 * 
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-17 10:45:47
 */
public interface CoreStorehouseService extends IService<CoreStorehouseEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<CoreStorehouseEntity> queryAllByHospitalId(Long hospitalId);
    
    CoreStorehouseEntity selectSupplyAddr(Long storehouseid);
    
    //HDI转换用  查询是否存在此原始标识对应的库房
    CoreStorehouseEntity selectByOrgdataid(String orgdataid);

    List<CoreStorehouseEntity> selectByOrgdataids(List<String> orgdataids);

    List<CoreStorehouseEntity> selectByStorehouseName(String storehouseName);
}

