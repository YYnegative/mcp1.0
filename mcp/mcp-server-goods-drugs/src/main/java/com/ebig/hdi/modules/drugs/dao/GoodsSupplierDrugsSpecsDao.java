package com.ebig.hdi.modules.drugs.dao;

import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 供应商药品规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:17:50
 */
public interface GoodsSupplierDrugsSpecsDao extends BaseMapper<GoodsSupplierDrugsSpecsEntity> {

	GoodsSupplierDrugsSpecsEntity selectByDrugsIdAndSpecs(@Param("drugsId") Long drugsId, @Param("specs") String specs);

	GoodsSupplierDrugsSpecsEntity selectByGuid(@Param("guid") String guid);

	List<GoodsSupplierDrugsSpecsEntity> selectListByDrugsId(@Param("drugsId") Long drugsId);
	
}
