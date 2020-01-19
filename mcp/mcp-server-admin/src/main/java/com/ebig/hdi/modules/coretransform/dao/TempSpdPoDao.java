package com.ebig.hdi.modules.coretransform.dao;

import com.ebig.hdi.modules.coretransform.entity.TempSpdPoEntity;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * temp_spd_po
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 21:27:53
 */
public interface TempSpdPoDao extends BaseMapper<TempSpdPoEntity> {
	
	TempSpdPoEntity selectByPoid(@Param("poid")String poid);
	
	Map<String, Object> getStorehouseid(@Param("orgdataid")String orgdataid);
	
	Map<String, Object> getHidAndDeptId(@Param("sourcesId")String sourcesId);
}
