package com.ebig.hdi.modules.refunds.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @athor: wenchao
 * Date: 2019/10/29
 * Time: 9:58
 * Description: 封装退货单页面查询参数
 */
@Data
public class RefundsMasterParam implements Serializable {
	private static final long serialVersionUID = 2837428378592017867L;

	/**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 库房名称
     */
    private String storeHouseName;

    /**
     * 退货单编号
     */
    private String refundsNo;


    /**
     * 退货开始时间
     */
    private String refundsTimeBeginStr;

    /**
     * 退货结束时间
     */
    private String refundsTimeEndStr;
    /**
     * 状态
     */
    private Integer status;


    /**
     * 部门过滤
     */
    private String fileterDept;

}
