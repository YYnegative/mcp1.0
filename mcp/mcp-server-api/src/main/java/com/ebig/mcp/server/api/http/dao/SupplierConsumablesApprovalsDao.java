package com.ebig.mcp.server.api.http.dao;

import com.ebig.hdi.common.entity.GoodsSupplierConsumablesApprovalsEntity;
import com.ebig.mcp.server.api.http.vo.GoodsSupplierConsumablesVo;

import java.util.List;

/**
 * @description: 供应商耗材批文持久化类
 * @author: wenchao
 * @time: 2019-10-15 15:36
 */
public interface SupplierConsumablesApprovalsDao {

    /**
     * 获取不存在的批文数据
     *
     * @param updateConsumables
     */
     List<GoodsSupplierConsumablesVo> getApprovals(List<GoodsSupplierConsumablesVo> updateConsumables);

    /**
     * 批量插入批文
     * @param approvalsList
     */
     void insertBatchApprovals(final List<GoodsSupplierConsumablesApprovalsEntity> approvalsList);
}
