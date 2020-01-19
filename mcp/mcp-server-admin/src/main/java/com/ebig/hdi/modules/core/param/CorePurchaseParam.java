package com.ebig.hdi.modules.core.param;

import java.io.Serializable;

import lombok.Data;

/**
 * @athor: wen
 * Date: 2019/8/31
 * Time: 9:58
 * Description: 封装采购单页面查询参数
 */
@Data
public class CorePurchaseParam implements Serializable {

    /** 
	 * 描述: TODO <br/>
	 * Fields  serialVersionUID : TODO <br/>
	 */
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
     * 医院采购计划编号
     */
    private String purPlanNo;

    /**
     * 采购开始时间
     */
    private String purPlanStartingTime;


    /**
     * 采购结束时间
     */
    private String purPlanEndTime;


    /**
     * 期望到货开始时间
     */
    private String expectStartingTime;

    /**
     * 期望到货结束时间
     */
    private String expectEndTime;

    /**
     * 状态
     */
    private Integer purchaseStatus;
}
