package com.ebig.hdi.modules.coretransform.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.TempDistributionEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-06-18 09:55:25
 */
public interface TempDistributionDao extends BaseMapper<TempDistributionEntity> {
	
	TempDistributionEntity selectByDistributionid(@Param("distributionid") String distributionid);
	
	void deleteTempDistribution(@Param("distributionid") String distributionid);
	
	Map<String, Object> getDeptIdAndHorgIdAndSupplierId(@Param("uorganid") String uorganid,@Param("horganid") String horganid);
}
