package com.ebig.mcp.server.api.http.service;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierDrugsVo;

import java.util.List;

/**
 * @description: 供应商药品服务类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface GoodsSupplierDrugsService {

    /**
     * 批量插入药品
     * @param list 供应商药品数据集合类
     * @param supplierInfo 供应商信息
     */
    void saveBatch(List<GoodsSupplierDrugsVo> list, OrgSupplierInfoEntity supplierInfo );
}
