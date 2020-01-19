package com.ebig.hdi.modules.drugs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsSpecsEntity;

/**
 * 平台药品规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:24
 */
public interface GoodsPlatformDrugsSpecsDao extends BaseMapper<GoodsPlatformDrugsSpecsEntity> {
	
	GoodsPlatformDrugsSpecsEntity selectByDrugsIdAndSpecs(@Param("drugsId") Long drugsId, @Param("specs") String specs);

	GoodsPlatformDrugsSpecsEntity selectByGuid(@Param("guid") String guid);

	List<GoodsPlatformDrugsSpecsEntity> selectListByDrugsId(@Param("drugsId") Long drugsId);
	
}
