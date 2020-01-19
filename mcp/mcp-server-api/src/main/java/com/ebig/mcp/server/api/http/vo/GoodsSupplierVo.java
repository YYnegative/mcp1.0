package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description: 耗材实体类
 * @author: wenchao
 * @time: 2019-10-15 15:17
 */
@Data
public class GoodsSupplierVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 供应商编码
     */
    @NotBlank
    private String supplierCode;

    /**
     * 阳光平台流水号
     */
    private String sunshinePno;

    /**
     * 商品规格编码
     */
    @NotBlank
    private String specsCode;

    /**
     * 商品规格
     */
    @NotBlank
    private String specs;

    /**
     * 全球唯一码
     */
    private String guid;

    /**
     * 商品属性(0:国产;1:进口)
     */
    @NotNull
    private Integer goodsNature;


    /**
     * 批准文号
     */
    @NotBlank
    private String approvals;

    /**
     * 商品分类名称
     */
    @NotBlank
    private String typeName;

    /**
     * 厂商名称
     */
    @NotBlank
    private String factoryName;

    /**
     * 状态(0:停用;1:启用)
     */
    @NotNull
    private Integer status;
    /**
     * 商品单位
     */
    @NotBlank
    private String goodsUnit;

    /**
     * 供货单位
     */
    private String supplyUnit;

    /**
     * 转换单位
     */
    private String convertUnit;


    /**
     * 储存方式
     */
    @NotBlank
    private String storeWay;

}
