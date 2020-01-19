package com.ebig.mcp.server.api.http.service;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierReagentVo;

import java.util.List;

/**
 * @description: 供应商试剂服务类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface GoodsSupplierReagentService {

    /**
     * 批量保存试剂
     * @param list
     */
     void saveBatch(List<GoodsSupplierReagentVo> list, OrgSupplierInfoEntity supplierInfo);
}
