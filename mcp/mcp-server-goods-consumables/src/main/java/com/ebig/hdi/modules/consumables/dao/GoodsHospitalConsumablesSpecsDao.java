package com.ebig.hdi.modules.consumables.dao;

import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesSpecsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 医院耗材规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:00
 */
public interface GoodsHospitalConsumablesSpecsDao extends BaseMapper<GoodsHospitalConsumablesSpecsEntity> {

	List<GoodsHospitalConsumablesSpecsEntity> selectListByConsumablesId(@Param("consumablesId") Long consumablesId);

	GoodsHospitalConsumablesSpecsEntity selectByGuid(@Param("guid") String guid);

	GoodsHospitalConsumablesSpecsEntity selectByConsumablesIdAndSpecs(@Param("consumablesId") Long consumablesId, @Param("specs") String specs);
	
}
