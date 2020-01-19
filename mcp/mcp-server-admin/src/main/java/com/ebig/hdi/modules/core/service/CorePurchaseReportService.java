package com.ebig.hdi.modules.core.service;

import com.ebig.hdi.modules.core.entity.CorePurchaseMonthReportEntity;
import com.ebig.hdi.modules.core.entity.CorePurchaseReportEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wen on 2019/8/13.
 */
public interface CorePurchaseReportService {
    /**
     * 统计数据
     * @param params
     * @return
     */
    List<CorePurchaseReportEntity> getListByMap(Map<String, Object> params);

    /**
     * 统计供应商当前年每月采购金额
     * @param supplieId
     * @return
     */
    List<CorePurchaseMonthReportEntity> getMonthDataBySupplieId(Long supplieId);
}
