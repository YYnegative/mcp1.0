package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesSpecsEntity;

import java.util.List;
import java.util.Map;

/**
 * 供应商耗材规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
public interface GoodsSupplierConsumablesSpecsService extends IService<GoodsSupplierConsumablesSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<GoodsSupplierConsumablesSpecsEntity> selectByConsumablesId(Long id);

	void insertOrUpdate(List<GoodsSupplierConsumablesSpecsEntity> goodsSupplierConsumablesSpecsList);
}

