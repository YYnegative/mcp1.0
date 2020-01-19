package com.ebig.hdi.modules.reagent.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.entity.GoodsSupplierReagentSpecsEntity;

/**
 * 供应商试剂规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
public interface GoodsSupplierReagentSpecsService extends IService<GoodsSupplierReagentSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<GoodsSupplierReagentSpecsEntity> selectListByReagentId(Long id);

	void save(GoodsSupplierReagentSpecsEntity goodsSupplierReagentSpecsEntity);

	void insertOrUpdate(List<GoodsSupplierReagentSpecsEntity> goodsSupplierReagentSpecsList);
}

