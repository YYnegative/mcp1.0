package com.ebig.hdi.modules.reagent.dao;

import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentSpecsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 医院试剂规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
public interface GoodsHospitalReagentSpecsDao extends BaseMapper<GoodsHospitalReagentSpecsEntity> {
	
	List<GoodsHospitalReagentSpecsEntity> selectListByReagentId(@Param("id") Long id);

	GoodsHospitalReagentSpecsEntity selectByReagenIdAndSpecs(@Param("reagenId")Long reagenId, @Param("specs")String specs);

	GoodsHospitalReagentSpecsEntity selectByGuid(@Param("guid")String guid);
}
