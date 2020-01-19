package com.ebig.hdi.modules.consumables.param;

import lombok.Data;

import java.io.Serializable;
/**
 * @author yzh
 */
@Data
public class GoodsSupplierConsumablesParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 耗材名称
     */
    private String consumablesName;
    /**
     * 商品属性
     */
    private Integer goodsNature;
    /**
     * 商品分类
     */
    private String typeName;
    /**
     * 状态（0：停用，1：启用）
     */
    private Integer status;
    /**
     * 厂商名称
     */
    private String factoryName;
}
