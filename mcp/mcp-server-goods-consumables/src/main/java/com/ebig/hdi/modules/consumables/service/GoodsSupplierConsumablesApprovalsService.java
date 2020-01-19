package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;

import java.util.List;
import java.util.Map;

/**
 * 供应商耗材批准文号
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
public interface GoodsSupplierConsumablesApprovalsService extends IService<GoodsSupplierConsumablesApprovalsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<GoodsSupplierConsumablesApprovalsEntity> selectByConsumablesId(Long id);

    void save(GoodsSupplierConsumablesApprovalsEntity goodsSupplierConsumablesApprovals);

	GoodsSupplierConsumablesApprovalsEntity selectByApprovals(String approvals);
}

