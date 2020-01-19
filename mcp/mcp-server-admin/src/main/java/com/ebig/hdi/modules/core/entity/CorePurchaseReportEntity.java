package com.ebig.hdi.modules.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wen on 2019/8/14.
 */
@Data
public class CorePurchaseReportEntity implements Serializable {


    /** 
	 * 描述: TODO <br/>
	 * Fields  serialVersionUID : TODO <br/>
	 */
	private static final long serialVersionUID = -6435490422075753320L;

	private Double totalPrice;//总价

    private String mark;//医院商品的唯一标识

    private String supplierGoodsName;//商品名称

}
