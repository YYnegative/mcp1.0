/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.job.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.modules.job.entity.ScheduleJobLogEntity;
import com.ebig.hdi.modules.job.service.ScheduleJobLogService;

/**
 * 定时任务日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
	@Autowired
	private ScheduleJobLogService scheduleJobLogService;
	
	/**
	 * 定时任务日志列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:schedule:log")
	public Hdi list(@RequestParam Map<String, Object> params){
		PageUtils page = scheduleJobLogService.queryPage(params);
		
		return Hdi.ok().put("page", page);
	}
	
	/**
	 * 定时任务日志信息
	 */
	@RequestMapping("/info/{logId}")
	public Hdi info(@PathVariable("logId") Long logId){
		ScheduleJobLogEntity log = scheduleJobLogService.selectById(logId);
		
		return Hdi.ok().put("log", log);
	}
}
