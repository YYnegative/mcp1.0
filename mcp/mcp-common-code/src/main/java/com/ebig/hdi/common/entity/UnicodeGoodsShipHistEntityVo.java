package com.ebig.hdi.common.entity;

import lombok.Data;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/12/30 0030 下午 20:34
 * @version： V1.0
 */
@Data
public class UnicodeGoodsShipHistEntityVo extends UnicodeGoodsShipHistEntity {

    private String userName;
    /**
     * 审批状态
     */
    private Integer approvalStatus;
}