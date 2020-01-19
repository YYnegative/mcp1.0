package com.ebig.mcp.server.api.http.service;

import com.ebig.hdi.common.entity.MasterDetailsCommonEntity;
import com.ebig.mcp.server.api.http.vo.PurchaseMenuCriteria;

import java.util.List;

/**
 * @Date:2019/10/21 0021
 * @Author: yy
 * @Desc:
 */
public interface IPurchaseMenuService {


    /**
     *
     * @param purchaseMenuCriteriaList 接收参数
     * @return 返回主细单列表
     */
    List<MasterDetailsCommonEntity> dealwithPurchaseMenu(List<PurchaseMenuCriteria> purchaseMenuCriteriaList);
}
