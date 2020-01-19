package com.ebig.hdi.modules.license.entity.vo;

import java.io.Serializable;

import com.ebig.hdi.modules.license.entity.LicenseGoodsInfoEntity;

import lombok.Data;

/**
 * 商品证照信息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
@Data
public class LicenseGoodsInfoVO extends LicenseGoodsInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 厂商名称
	 */
	private String factoryName;
	/**
	 * 分类名称
	 */
	private String classifyName;
	/**
	 * 证照状态
	 */
	private Integer licenseStatus;
	
	/**
	 * 证照名称/编号
	 */
	private String nameOrNumber;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 是否默认排序，1：是
	 */
	private Integer isDefaultOrder;
	
	/**
	 * 接收前端的开始时间
	 */
	private String beginTimeStr;
	
	/**
	 * 接收前端的结束时间
	 */
	private String endTimeStr;
	
	/**
	 * 分页时候的部门权限过滤
	 */
	private String fileterDept;
	
	/**
	 * 父证照
	 */
	private LicenseGoodsInfoVO parentAgentLicense;
	
	/**
	 * 子证照
	 */
	private LicenseGoodsInfoVO childAgentLicense;
}
