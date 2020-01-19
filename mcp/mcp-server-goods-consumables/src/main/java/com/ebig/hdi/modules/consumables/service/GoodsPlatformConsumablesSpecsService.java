package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesSpecsEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;

import java.util.List;
import java.util.Map;

/**
 * 平台耗材规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:45
 */
public interface GoodsPlatformConsumablesSpecsService extends IService<GoodsPlatformConsumablesSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(GoodsPlatformConsumablesSpecsEntity goodsPlatformConsumablesSpecs);

	void insertOrUpdate(List<GoodsPlatformConsumablesSpecsEntity> goodsPlatformConsumablesSpecsList);

	List<GoodsPlatformConsumablesSpecsEntity> selectListByConsumablesId(Long consumablesId);

	Map<String, Object> save(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo);

	GoodsPlatformConsumablesSpecsEntity selectByConsumablesIdAndSpecs(Long id, String s);
}

