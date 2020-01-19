package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.entity.GoodsSupplierConsumablesSpecsEntity;

import java.util.List;

/**
 * @description: 供应商耗材规格持久化类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface SupplierConsumablesSpecsDao {


    /**
     * 批量插入规格数据
     *
     * @param list
     */
    void insertSpecs(List<GoodsSupplierConsumablesSpecsEntity> list);

    void updateSpecs(List<GoodsSupplierConsumablesSpecsEntity> list);

    /**
     * 获取不存在的规格数据
     *
     * @param list 查询条件集合
     * @return 不存在的规格数据
     */
    List<GoodsSupplierConsumablesSpecsEntity> getNotExistSpecs(List<GoodsSupplierConsumablesSpecsEntity> list);
}
