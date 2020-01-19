package com.ebig.hdi.modules.refunds.entity.vo;

import com.ebig.hdi.modules.refunds.entity.RefundsMasterEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/10/30 0030 下午 14:23
 * @version： V1.0
 */
@Data
public class SaveRefundsMasterVo  extends RefundsMasterEntity implements Serializable {

    /**
     * 退货主单Id
     */
    private Long refundsMasterId;
    /**
     * 商品
     */
    private String goodsType;
    /**
     * 医院名称
     */
    @NotBlank(message = "医院名称不能为空")
    private String hospitalName;

    /**
     * 库房名称
     */
    @NotBlank(message= "库房名称不能为空")
    private String storeHouseName;

    /**
     * 医院退货申请单编号
     */
    private String refundsApplyNo;
    /**
	 * 退货单编号
	 */
    private String refundsNo;
    /**
     * 退货时间
     */
    @NotNull(message="退货时间不能为空")
    private Date refundsTime;

    /**
     * 细单
     */
    private List<SaveRefundsDetailVo> saveRefundsDetailVoList;
}