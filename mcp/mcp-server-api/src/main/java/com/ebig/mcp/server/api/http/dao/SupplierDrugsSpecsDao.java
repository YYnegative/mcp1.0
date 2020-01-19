package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.entity.GoodsSupplierDrugsSpecsEntity;

import java.util.List;

/**
 * @description: 供应商药品规格持久化类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface SupplierDrugsSpecsDao {


    /**
     * 更新规格数据
     *
     * @param list
     */
     void updateSpecs(List<GoodsSupplierDrugsSpecsEntity> list);


    /**
     * 批量插入规格数据
     *
     * @param list
     */
    void insertSpecs(List<GoodsSupplierDrugsSpecsEntity> list);

    /**
     * 获取不存在的规格数据
     *
     * @param list 查询条件集合
     * @return 不存在的规格数据
     */
     List<GoodsSupplierDrugsSpecsEntity> getNotExistSpecs(List<GoodsSupplierDrugsSpecsEntity> list);
}
