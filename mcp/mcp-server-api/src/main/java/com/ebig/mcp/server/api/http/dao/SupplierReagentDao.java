package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierReagentVo;

import java.util.List;
import java.util.Map;

/**
 * @description: 供应商药品持久化类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface SupplierReagentDao {

    /**
     * 查询已经存在的试剂数据
     *
     * @param subList
     * @param supplierInfo
     * @return
     */
     List<GoodsSupplierReagentVo> getSupplierReagent(List<GoodsSupplierReagentVo> subList, OrgSupplierInfoEntity supplierInfo);

    /**
     * 更新试剂
     *
     * @param list
     */
     List<GoodsSupplierReagentVo> update(List<GoodsSupplierReagentVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map);


    /**
     * 批量插入
     *
     * @param list
     */
     List<GoodsSupplierReagentVo> insert(final List<GoodsSupplierReagentVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map);
}
