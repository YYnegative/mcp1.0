package com.ebig.hdi.modules.coretransform.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.TempDistributiondtlEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-06-18 10:59:10
 */
public interface TempDistributiondtlDao extends BaseMapper<TempDistributiondtlEntity> {
	
	List<TempDistributiondtlEntity> selectTempDistributiondtl();
	
	void deleteTempDistributiondtl(@Param("distributiondtlid") String distributiondtlid);
}
