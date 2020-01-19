package com.ebig.hdi.modules.reagent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;

/**
 * 供应商试剂规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
public interface GoodsSupplierReagentSpecsDao extends BaseMapper<GoodsSupplierReagentSpecsEntity> {
	
	List<GoodsSupplierReagentSpecsEntity> selectListByReagentId(@Param("id") Long id);

	GoodsSupplierReagentSpecsEntity selectByReagenIdAndSpecs(@Param("reagenId")Long reagenId, @Param("specs")String specs);

	GoodsSupplierReagentSpecsEntity selectByReagenIdAndGuid(@Param("reagenId")Long reagenId,@Param("guid")String guid);
}
