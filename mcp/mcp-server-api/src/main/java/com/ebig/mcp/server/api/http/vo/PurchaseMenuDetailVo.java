package com.ebig.mcp.server.api.http.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 类功能说明： 返回采购单细单类
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/10/28 0028 下午 14:45
 * @version： V1.0
 */
@Data
public class PurchaseMenuDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 采购细单ID
     */
    private Long detailId;
    /**
     * 商品规格编码
     */
    private String goodsSpecsCode;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品计量单位
     */
    private String goodsUnit;
    /**
     * 商品数量
     */
    private Double goodsAmount;
    /**
     * 商品单价
     */
    private Double goodsPrice;
    /**
     * 采购细单备注
     */
    private String detailRemark;
    /**
     * 生产厂商名称
     */
    private String factoryName;
}