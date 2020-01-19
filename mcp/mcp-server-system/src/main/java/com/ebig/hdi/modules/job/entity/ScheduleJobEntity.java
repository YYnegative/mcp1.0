/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.job.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 定时任务
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("schedule_job")
public class ScheduleJobEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 任务调度参数key
	 */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";
	
	/**
	 * 任务id
	 */
	@TableId
	private Long jobId;

	/**
	 * spring bean名称
	 */
	@NotBlank(message="bean名称不能为空")
	private String beanName;
	
	/**
	 * 参数
	 */
	private String params;
	
	/**
	 * cron表达式
	 */
	@NotBlank(message="cron表达式不能为空")
	private String cronExpression;

	/**
	 * 任务状态
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	/**
	 * 任务执行开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date beginTime;
	
	/**
	 *任务执行结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

}
