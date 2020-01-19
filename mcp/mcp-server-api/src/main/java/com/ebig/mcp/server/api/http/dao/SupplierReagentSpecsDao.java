package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.entity.GoodsSupplierReagentSpecsEntity;

import java.util.List;

/**
 * @description: 供应商药品规格持久化类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface SupplierReagentSpecsDao {

    /**
     * 批量插入规格
     * @param list
     */
    void insertSpecs(List<GoodsSupplierReagentSpecsEntity> list);

    /**
     * 更新规格
     *
     * @param list
     */
     void updateSpecs(List<GoodsSupplierReagentSpecsEntity> list);

    /**
     * 获取不存在的规格数据
     *
     * @param list 查询条件集合
     * @return 不存在的规格数据
     */
    List<GoodsSupplierReagentSpecsEntity> getNotExistSpecs(List<GoodsSupplierReagentSpecsEntity> list);
}
