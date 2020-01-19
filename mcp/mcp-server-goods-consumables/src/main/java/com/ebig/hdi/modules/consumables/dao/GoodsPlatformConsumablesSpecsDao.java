package com.ebig.hdi.modules.consumables.dao;

import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesSpecsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 平台耗材规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:45
 */
public interface GoodsPlatformConsumablesSpecsDao extends BaseMapper<GoodsPlatformConsumablesSpecsEntity> {

	GoodsPlatformConsumablesSpecsEntity selectByConsumablesIdAndSpecs(@Param("consumablesId") Long consumablesId, @Param("specs") String specs);

	GoodsPlatformConsumablesSpecsEntity selectByGuid(@Param("guid") String guid);

	List<GoodsPlatformConsumablesSpecsEntity> selectListByConsumablesId(@Param("consumablesId") Long consumablesId);
	
}
