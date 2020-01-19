package com.ebig.hdi.modules.consumables.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;

/**
 * 医院耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:01
 */
public interface GoodsHospitalConsumablesService extends IService<GoodsHospitalConsumablesEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo);

	void update(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo);

	void delete(Long[] ids);

	GoodsHospitalConsumablesEntityVo selectHospitalConsumablesById(Long id);

	void toggle(Map<String, Object> params);
	
	//定时任务用
	void updateIsMatch(Long goodsId);
	
    //HDI转换用
	Map<String, Object> selectBySourcesId(String sourcesSpecsId);
	
	GoodsHospitalConsumablesEntity selectByGoodsNameAndFactoryNameAndHospitalId(String goodsName,String factoryName, Long hospitalId);
}

