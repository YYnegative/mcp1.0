package com.ebig.hdi.modules.coretransform.dao;

import com.ebig.hdi.modules.coretransform.entity.TempHpurorderdtlEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 医院采购明细
 * 
 * @author frink
 * @email 
 * @date 2019-06-21 10:33:47
 */
public interface TempHpurorderdtlDao extends BaseMapper<TempHpurorderdtlEntity> {
	
	List<TempHpurorderdtlEntity> selectTempHpurorderdtl();
	
	void deleteTempHpurorderdtl(@Param("hpurorderdtlid") String hpurorderdtlid);
	
}
