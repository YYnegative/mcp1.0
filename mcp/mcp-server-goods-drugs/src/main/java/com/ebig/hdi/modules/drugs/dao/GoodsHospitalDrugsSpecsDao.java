package com.ebig.hdi.modules.drugs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;

/**
 * 医院药品规格
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:22:44
 */
public interface GoodsHospitalDrugsSpecsDao extends BaseMapper<GoodsHospitalDrugsSpecsEntity> {
	
	GoodsHospitalDrugsSpecsEntity selectByDrugsIdAndSpecs(@Param("drugsId") Long drugsId, @Param("specs") String specs);

	GoodsHospitalDrugsSpecsEntity selectByGuid(@Param("guid") String guid);

	List<GoodsHospitalDrugsSpecsEntity> selectListByDrugsId(@Param("drugsId") Long drugsId);
	
}
