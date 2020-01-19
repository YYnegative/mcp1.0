package com.ebig.hdi.modules.report.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CoreSupplyDetailVo implements Serializable{

    /**
     * 商品名称
     */
    private String goodsname;

    /**
     * 商品规格
     */
    private String goodstype;

    /**
     * 商品单位
     */
    private String goodsunit;
    /**
     * 供货数量
     */
    private Double supplyQty;
    /**
     * 供货单价
     */
    private Double supplyUnitprice;
    /**
     * 生产批号
     */
    private String lotno;
    /**
     * 生产日期
     */
    private String proddate;
    /**
     * 失效日期
     */
    private String invadate;
    /**
     * 生产厂家名称
     */
    private String factoryName;

    /**
     * 批准文号
     */
    private String approvals;

    /**
     * 储存方式
     */
    private String storeWay;

    /**
     * 供货单标识
     */
    private Long supplyMasterId;
    /**
     * 供货单编号
     */
    private String supplyno;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 平台医院名称
     */
    private String hospitalName;
    /**
     * 平台医院库房名称
     */
    private String storehouseName;

    /**
     * 采购计划编号
     */
    private String purplanno;
    /**
     * 供货时间
     */
    private String supplyTime;
    /**
     * 供货类型 0 非票货同行 1 票货同行
     */
    private String supplyType;
    /**
     * 数量合计
     */
    private Integer total;

    /**
     * 金额合计
     */
    private Double totalAmount;



}
