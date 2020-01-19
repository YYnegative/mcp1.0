package com.ebig.hdi.modules.license.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 证照医院审批
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:30
 */
@Data
@TableName("hdi_license_hospital_examine")
public class LicenseHospitalExamineEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 证照id
	 */
	private Long licenseId;
	/**
	 * 证照类型(0:商品证照;1:供应商证照;2:厂商证照;3:代理商证照)
	 */
	private Integer licenseType;
	/**
	 * 分类id
	 */
	private Long classifyId;
	/**
	 * 证照名称
	 */
	private String name;
	/**
	 * 证照编号
	 */
	private String number;
	/**
	 * 效期开始时间
	 */
	private Date beginTime;
	/**
	 * 效期结束时间
	 */
	private Date endTime;
	/**
	 * 证照图片
	 */
	private String picUrl;
	/**
	 * 业务id(商品id或供应商id)
	 */
	private Long businessId;
	/**
	 * 业务名称(商品名称或供应商名称)
	 */
	private String businessName;
	/**
	 * 医院id
	 */
	private Long hospitalId;
	/**
	 * 医院名称
	 */
	private String hospitalName;
	/**
	 * 所属机构
	 */
	private Long deptId;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 审核状态(0:待审批;1:审核通过;2:审核不通过)
	 */
	private Integer status;
	/**
	 * 审批意见
	 */
	private String examineOpinion;
	/**
	 * 审批时间
	 */
	private Date examineTime;

}
