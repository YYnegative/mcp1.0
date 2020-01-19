package com.ebig.hdi.modules.reagent.dao;

import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentSpecsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 平台试剂规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
public interface GoodsPlatformReagentSpecsDao extends BaseMapper<GoodsPlatformReagentSpecsEntity> {

	GoodsPlatformReagentSpecsEntity selectByReagenIdAndSpecs(@Param("reagenId")Long reagenId, @Param("specs")String specs);

	GoodsPlatformReagentSpecsEntity selectByGuid(@Param("guid")String guid);

	List<GoodsPlatformReagentSpecsEntity> selectListByReagentId(@Param("reagentId")Long reagentId);

    GoodsPlatformReagentSpecsEntity selectByReagentIdAndSpecs(@Param("ReagentId")Long id, @Param("specs")String specs);
}
