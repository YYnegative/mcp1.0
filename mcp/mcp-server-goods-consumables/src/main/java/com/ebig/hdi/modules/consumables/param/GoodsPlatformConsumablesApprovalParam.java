package com.ebig.hdi.modules.consumables.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @athor: yzh
 * Date: 2019/11/26
 * Time: 16：00
 * Description: 封装平台商品信息查询参数
 */
@Data
public class GoodsPlatformConsumablesApprovalParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 审批状态
     */
    private Integer checkStatus;
    /**
     * 耗材名称
     */
    private String consumablesName;
    /**
     * 厂商名称
     */
    private String factoryName;
    /**
     * 商品属性（0：国产，1：进口）
     */
    private Integer goodsNature;
    /**
     * 状态（0：停用，1：启用）
     */
    private Integer status;
    /**
     * 商品分类ID
     */
    private Long typeId;
    /**
     * 商品ID集合
     */
    private List<Long> typeIds;
}
