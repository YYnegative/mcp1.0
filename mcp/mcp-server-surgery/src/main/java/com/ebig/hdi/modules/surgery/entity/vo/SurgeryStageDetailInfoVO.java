package com.ebig.hdi.modules.surgery.entity.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.surgery.entity.SurgeryStageDetailInfoEntity;

import lombok.Data;

/**
 * 手术跟台目录明细表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@Data
public class SurgeryStageDetailInfoVO extends SurgeryStageDetailInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String goodsName;
	
	private String factoryName;
	
	private String specs;
	
	private String goodsUnit;
	
	/**
	 * 批号生产日期Str
	 */
	private String plotProddateStr;
	
	/**
	 * 批号失效日期Str
	 */
	private String plotValidtoStr;
	
	/**
	 * 灭菌批号生产日期Str
	 */
	private String slotProddateStr;
	
	/**
	 * 灭菌批号失效日期Str
	 */
	private String slotValidtoStr;

}
