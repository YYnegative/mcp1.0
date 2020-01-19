package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;

import java.util.List;
import java.util.Map;

/**
 * 医院耗材规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:00
 */
public interface GoodsHospitalConsumablesSpecsService extends IService<GoodsHospitalConsumablesSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<GoodsHospitalConsumablesSpecsEntity> selectListByConsumablesId(Long consumablesId);

	void save(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo);

	void save(GoodsHospitalConsumablesSpecsEntity goodsHospitalConsumablesSpecs);

	void insertOrUpdate(List<GoodsHospitalConsumablesSpecsEntity> goodsHospitalConsumablesSpecsList);
}

