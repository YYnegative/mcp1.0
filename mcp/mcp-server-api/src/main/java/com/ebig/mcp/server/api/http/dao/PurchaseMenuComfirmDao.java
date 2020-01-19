package com.ebig.mcp.server.api.http.dao;

import com.ebig.mcp.server.api.http.vo.DetailItem;
import com.ebig.mcp.server.api.http.vo.Item;

import java.util.List;

/**
 * Date:2019/10/28 0028
 * Author: yy
 * Desc:
 */
public interface PurchaseMenuComfirmDao {
    void updatePurchaseMuneStatus(String purchaseStatus,Long purchaseMasterId);

    Item findPurchaseMaster(Long masterId, String supplierCode);

    List<DetailItem> findPurchaseDetail(Long purchaseMasterId);
}
