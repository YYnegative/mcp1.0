package com.ebig.hdi.modules.refunds.entity.vo;

import com.ebig.hdi.modules.refunds.entity.RefundsDetailEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类功能说明：
 * 类修改者：
 * 修改日期：
 * 修改说明：
 * company：广东以大供应链管理有限公司
 *
 * @author： yy
 * @Date： 2019/10/30 0030 下午 14:28
 * @version： V1.0
 */
@Data
public class SaveRefundsDetailVo  extends RefundsDetailEntity implements Serializable {
    /**
     * 退货细单id
     */
    private Long refundsDetailId;
    /**
     * 供货单编号
     */
    @NotBlank(message = "供货单编号不能为空")
    private String supplyNo;
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;
    /**
     * 生产厂商
     */
    @NotBlank(message="生产厂商不能为空")
    private String factoryName;
    /**
     * 商品规格
     */
    @NotBlank(message = "商品规格不能为空")
    private String goodsSpecs;
    /**
     * 商品单位
     */
    @NotBlank(message = "商品单位不能为空")
    private String goodsUnit;

    /**
     * 退货数量
     */
    @NotNull(message="退货数量不能为空")
    private Integer refundsNumber;
    /**
     * 退货单价
     */
    @NotNull(message = "退货单价不能为空")
    private BigDecimal refundsPrice;
    /**
     * 退货原因
     */
    @NotBlank(message = "退货原因不能为空")
    private String refundsRemark;
    /**
     * 生成批号
     */
    @NotNull(message = "生成批号不能为空")
    private String lotNo;

}