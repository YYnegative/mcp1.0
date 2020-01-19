package com.ebig.hdi.modules.drugs.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsHospitalDrugsEntityVo;

/**
 * 医院药品信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:22:45
 */
public interface GoodsHospitalDrugsService extends IService<GoodsHospitalDrugsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo);

	void update(GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo);

	void delete(Long[] ids);

	GoodsHospitalDrugsEntityVo selectHospitalDrugsById(Long id);

	void toggle(Map<String, Object> params);
	
	//定时任务用
	void updateIsMatch(Long goodsId);
	
	GoodsHospitalDrugsEntity selectByGoodsNameAndFactoryNameAndHospitalId(String goodsName, String factoryName, Long hospitalId);

	
	//HDI转换用
	Map<String, Object> selectBySourcesId(String sourcesSpecsId);

}

