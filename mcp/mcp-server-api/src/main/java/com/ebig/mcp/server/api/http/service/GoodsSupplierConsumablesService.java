package com.ebig.mcp.server.api.http.service;


import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierConsumablesVo;

import java.util.List;

/**
 * @description: 供应商耗材服务类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface GoodsSupplierConsumablesService {

    /**
     * 批量插入或更新
     * @param list 耗材数据集合
     * @param supplierInfo 供应商信息
     */
    void batchSave(List<GoodsSupplierConsumablesVo> list, OrgSupplierInfoEntity supplierInfo);
}
