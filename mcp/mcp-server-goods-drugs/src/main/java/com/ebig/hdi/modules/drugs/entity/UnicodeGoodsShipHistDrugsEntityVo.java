package com.ebig.hdi.modules.drugs.entity;

import lombok.Data;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/12/31 0031 上午 9:40
 * @version： V1.0
 */
@Data
public class UnicodeGoodsShipHistDrugsEntityVo extends UnicodeGoodsShipHistDrugsEntity {
    private String userName;
    /**
     * 审批状态
     */
    private Integer approvalStatus;
}