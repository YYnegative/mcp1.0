package com.ebig.hdi.modules.surgery.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 手术信息表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:32
 */
@TableName("mcp_surgery_info")
@Data
public class SurgeryInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 手术单id
	 */
	@TableId
	private Long id;
	/**
	 * 医院id
	 */
	private Long hospitalId;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 所属机构
	 */
	private Long deptId;
	/**
	 * 手术单号
	 */
	private String surgeryNo;
	/**
	 * 手术名称
	 */
	private String surgeryTitle;
	/**
	 * 手术时间
	 */
	private Date surgeryDate;
	/**
	 * 住院号码
	 */
	private String customNo;
	/**
	 * 病人姓名
	 */
	private String customName;
	/**
	 * 性别(1:男;2:女)
	 */
	private Integer customSex;
	/**
	 * 病人年龄
	 */
	private Integer customAge;
	/**
	 * 申请时间
	 */
	private Date apppoveDate;
	/**
	 * 状态(0:作废;1:初始;2:进行中;3:清点完成)
	 */
	private Integer status;
	/**
	 * 来源id，记录从HDI转化来时的原始手术单ID
	 */
	private String sourceId;

}
