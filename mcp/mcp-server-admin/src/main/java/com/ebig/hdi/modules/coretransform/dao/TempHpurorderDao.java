package com.ebig.hdi.modules.coretransform.dao;

import com.ebig.hdi.modules.coretransform.entity.TempHpurorderEntity;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 医院采购订单
 * 
 * @author frink
 * @email 
 * @date 2019-06-21 10:34:03
 */
public interface TempHpurorderDao extends BaseMapper<TempHpurorderEntity> {
	
	TempHpurorderEntity selectTempHpurorder(@Param("hpurorderid") String hpurorderid);
		
	Map<String, Object> getDeptIdAndHorgIdAndSupplierId(@Param("uorganid") String uorganid,@Param("horganid") String horganid);

	void deleteTempHpurorder(@Param("hpurorderid") String hpurorderid);

	Map<String, Object> selectHospitalConsumablesBySourcesSpecsId(@Param("sourcesSpecsId") String sourcesSpecsId);

	Map<String, Object> selectHospitalDrugsBySourcesSpecsId(@Param("sourcesSpecsId") String sourcesSpecsId);

	Map<String, Object> selectHospitalReagentBySourcesSpecsId(@Param("sourcesSpecsId") String sourcesSpecsId);

}
