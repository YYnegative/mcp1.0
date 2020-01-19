package com.ebig.hdi.modules.drugs.vo;


import lombok.Builder;
import lombok.Data;

import java.util.List;


/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/11/28 0028 上午 11:15
 * @version： V1.0
 */
@Data
@Builder
public class GoodsPlatformDrugsParams {
    /**
     * 商品统一编码
     */
    private String goodsUnicode;

    /**
     * 药品名称
     */
    private String drugsName;

    /**
     * 商品属性(0:国产;1:进口)
     */
    private Integer goodsNature;
    /**
     * 商品分类
     */
    private Long typeId;

    /**
     * 生产厂商
     */
    private String factoryName;
    /**
     * 批准文号
     */
    private String approvals;
    /**
     * 状态(0:停用;1:启用)
     */
    private Integer status;
    /**
     * 审批状态(0.待审批 1.审批通过 2.审批不通过)
     */
    private Integer checkStatus;

    private List<Long> typeIds;
}