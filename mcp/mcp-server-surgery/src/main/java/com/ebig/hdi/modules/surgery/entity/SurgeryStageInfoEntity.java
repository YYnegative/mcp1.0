package com.ebig.hdi.modules.surgery.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 手术跟台目录表
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-06-11 10:15:31
 */
@TableName("mcp_surgery_stage_info")
@Data
public class SurgeryStageInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 跟台目录id
	 */
	@TableId
	private Long id;
	/**
	 * 原跟台目录主单id
	 */
	private String sourceStageId;
	/**
	 * 医院id
	 */
	private Long hospitalId;
	/**
	 * 所属机构
	 */
	private Long deptId;
	/**
	 * 跟台目录编号
	 */
	private String surgeryStageNo;
	/**
	 * 手术单id
	 */
	private Long surgeryId;
	/**
	 * 跟台目录类型(1:前绑合格证;2:后补合格证)
	 */
	private Integer surgeryStageType;
	/**
	 * 跟台目录状态(1:未提交;2:已提交;3:已验收;4:部分验收;5:拒收)
	 */
	private Integer status;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 数据来源(1:手术单;2:跟台目录添加)
	 */
	private Integer dataSource;
	/**
	 * 是否上传(0:未上传;1:已上传)
	 */
	private Integer isUpload;
}
