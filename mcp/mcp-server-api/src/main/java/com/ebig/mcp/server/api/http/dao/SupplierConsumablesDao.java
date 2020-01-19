package com.ebig.mcp.server.api.http.dao;


import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierConsumablesVo;

import java.util.List;
import java.util.Map;

/**
 * @description: 供应商耗材持久化类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface SupplierConsumablesDao {

    List<GoodsSupplierConsumablesVo> insert(List<GoodsSupplierConsumablesVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map);

    List<GoodsSupplierConsumablesVo> update(List<GoodsSupplierConsumablesVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map);

    /**
     * 查询已经存在的耗材数据
     *
     * @param subList
     * @param supplierInfo
     * @return
     */
    List<GoodsSupplierConsumablesVo> getGoodsSupplierConsumablesVos(List<GoodsSupplierConsumablesVo> subList, OrgSupplierInfoEntity supplierInfo);

}
