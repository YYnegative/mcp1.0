package com.ebig.hdi.modules.consumables.dao;

import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 供应商耗材规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
public interface GoodsSupplierConsumablesSpecsDao extends BaseMapper<GoodsSupplierConsumablesSpecsEntity> {
	
	List<GoodsSupplierConsumablesSpecsEntity> selectByConsumablesId(@Param("id") Long id);

	GoodsSupplierConsumablesSpecsEntity selectByConsumablesIdAndSpecs(@Param("consumablesId")Long consumablesId, @Param("specs")String specs);

	GoodsSupplierConsumablesSpecsEntity selectByGuid(@Param("guid")String guid);
	
}
