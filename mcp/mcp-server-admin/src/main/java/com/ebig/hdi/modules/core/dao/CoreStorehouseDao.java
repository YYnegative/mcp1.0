package com.ebig.hdi.modules.core.dao;

import com.ebig.hdi.modules.core.entity.CoreStorehouseEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-17 10:45:47
 */
public interface CoreStorehouseDao extends BaseMapper<CoreStorehouseEntity> {
	List<CoreStorehouseEntity> queryAllByHospitalId(@Param("hospitalId") Long hospitalId);
	
	CoreStorehouseEntity selectSupplyAddr(@Param("storehouseid")Long storehouseid);

    //HDI转换用  查询是否存在此原始标识对应的库房
    CoreStorehouseEntity selectByOrgdataid(@Param("orgdataid")String orgdataid);

    List<CoreStorehouseEntity> selectByOrgdataids(@Param("orgdataids") List<String> orgdataids);

    List<CoreStorehouseEntity> selectByStoreName(@Param("storehouseName")String storehouseName);
}
