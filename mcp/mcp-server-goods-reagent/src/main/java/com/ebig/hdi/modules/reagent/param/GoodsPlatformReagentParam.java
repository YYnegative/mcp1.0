package com.ebig.hdi.modules.reagent.param;

import lombok.Data;

import java.util.List;

/**
 * @program: mcp
 * @description: 封装平台试剂信息查询参数
 * @email 791203472@qq.com
 * @author: ZhengHaiWen
 * @create: 2019-11-28 09:58
 **/

@Data
public class GoodsPlatformReagentParam {

    private static final long serialVersionUID = 201911280958L;

    /**
     * 试剂名称
     */
    private String reagentName;

    /**
     * 商品属性
     */
    private String goodsNature;

    /**
     * 商品分类
     */
    private String typeId;

    /**
     * 规格
     */
    private String specs;

    /**
     * 生产厂商
     */
    private String factoryName;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 批准文号
     */
    private String approvals;

    /**
     * 审批状态
     */
    private Integer checkStatus;
    /**
     * 商品分类列表
     */
    private List<Long> typeIds;
}
