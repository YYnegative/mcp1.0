package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 类功能说明：采购单细单类
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @date： 2019/10/25 9:29
 * @version： V1.0
 */
@Data
public class DetailItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 采购细单ID
     */
    private Long purchaseDetailId;
    /**
     * 原医院商品规格编码
     */
    private String yhgoodsno;
    /**
     * 原医院商品规格名称
     */
    private String yhgoodstypename;
    /**
     * 医院采购商品单位
     */
    private String hgoodsunit;
    /**
     * 医院采购商品id
     */
    private String hgoodsid;
    /**
     * 医院采购商品规格id
     */
    private String hgoodstypeid;
    /**
     * 商品类型
     */
    private String goodsclass;
    /**
     * 医院采购数量
     */
    private Double hqty;
    /**
     * 采购单价
     */
    private Double hunitprice;
    /**
     * 备注
     */
    private String memo;

    /**
     * 供应商商品规格编码
     */
    private String supplierGoodsSpecsCode;

    /**
     * 供应商商品名称
     */
    private String supplierGoodsName;

    /**
     * 供应商厂商名称
     */
    private String supplierFactoryName;
}