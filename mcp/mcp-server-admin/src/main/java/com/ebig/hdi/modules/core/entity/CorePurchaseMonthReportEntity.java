package com.ebig.hdi.modules.core.entity;

import lombok.Data;

/**
 * 采购金额月统计实体类
 * Created by wen on 2019/8/13.
 */
@Data
public class CorePurchaseMonthReportEntity {


    /**
     * 日期
     */
    private String recordDate;

    /**
     * 总价
     */
    private Double totalPrice;




}
