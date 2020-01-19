package com.ebig.hdi.modules.drugs.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsEntityVo;

/**
 * 平台药品规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:24
 */
public interface GoodsPlatformDrugsSpecsService extends IService<GoodsPlatformDrugsSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
	void save(GoodsPlatformDrugsEntityVo goodsPlatformDrugsEntityVo) throws Exception;

	void save(GoodsPlatformDrugsSpecsEntity goodsPlatformDrugsSpecs);

	void insertOrUpdate(List<GoodsPlatformDrugsSpecsEntity> goodsPlatformDrugsSpecsList) throws Exception;
	
	List<GoodsPlatformDrugsSpecsEntity> selectListByDrugsId(Long drugsId);

	GoodsPlatformDrugsSpecsEntity selectByDrugsIdAndSpecs(Long id, String specs);
}

