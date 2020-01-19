package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.entity.OrgSupplierInfoEntity;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierDrugsVo;

import java.util.List;
import java.util.Map;

/**
 * @description: 供应商药品持久化类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface SupplierDrugsDao {

    /**
     * 查询已经存在的药品数据
     *
     * @param subList
     * @param supplierInfo
     * @return
     */
    List<GoodsSupplierDrugsVo> getList(List<GoodsSupplierDrugsVo> subList, OrgSupplierInfoEntity supplierInfo);

    /**
     * 更新药品
     * @param list
     * @param supplierInfo
     * @param map
     * @return
     */
    List<GoodsSupplierDrugsVo> update(List<GoodsSupplierDrugsVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map);

    /**
     * 批量插入
     *
     * @param list
     */
     List<GoodsSupplierDrugsVo> insert(List<GoodsSupplierDrugsVo> list, OrgSupplierInfoEntity supplierInfo, Map<String, Long> map);
}
