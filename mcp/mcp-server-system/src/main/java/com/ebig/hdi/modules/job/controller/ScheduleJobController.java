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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebig.hdi.common.annotation.SysLog;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;
import com.ebig.hdi.modules.job.service.ScheduleJobService;

/**
 * 定时任务
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	/**
	 * 定时任务列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:schedule:list")
	public Hdi list(@RequestParam Map<String, Object> params){
		PageUtils page = scheduleJobService.queryPage(params);

		return Hdi.ok().put("page", page);
	}
	
	/**
	 * 定时任务信息
	 */
	@RequestMapping("/info/{jobId}")
	@RequiresPermissions("sys:schedule:info")
	public Hdi info(@PathVariable("jobId") Long jobId){
		ScheduleJobEntity schedule = scheduleJobService.selectById(jobId);
		
		return Hdi.ok().put("schedule", schedule);
	}
	
	/**
	 * 保存定时任务
	 */
	@SysLog("保存定时任务")
	@RequestMapping("/save")
	@RequiresPermissions("sys:schedule:save")
	public Hdi save(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
		
		scheduleJobService.saveJob(scheduleJob);
		
		return Hdi.ok();
	}
	
	/**
	 * 修改定时任务
	 */
	@SysLog("修改定时任务")
	@RequestMapping("/update")
	@RequiresPermissions("sys:schedule:update")
	public Hdi update(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
				
		scheduleJobService.update(scheduleJob);
		
		return Hdi.ok();
	}
	
	/**
	 * 删除定时任务
	 */
	@SysLog("删除定时任务")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:schedule:delete")
	public Hdi delete(@RequestBody Long[] jobIds){
		scheduleJobService.deleteBatch(jobIds);
		
		return Hdi.ok();
	}
	
	/**
	 * 立即执行任务
	 */
	@SysLog("立即执行任务")
	@RequestMapping("/run")
	@RequiresPermissions("sys:schedule:run")
	public Hdi run(@RequestBody Long[] jobIds){
		scheduleJobService.run(jobIds);
		
		return Hdi.ok();
	}
	
	/**
	 * 暂停定时任务
	 */
	@SysLog("暂停定时任务")
	@RequestMapping("/pause")
	@RequiresPermissions("sys:schedule:pause")
	public Hdi pause(@RequestBody Long[] jobIds){
		scheduleJobService.pause(jobIds);
		
		return Hdi.ok();
	}
	
	/**
	 * 恢复定时任务
	 */
	@SysLog("恢复定时任务")
	@RequestMapping("/resume")
	@RequiresPermissions("sys:schedule:resume")
	public Hdi resume(@RequestBody Long[] jobIds){
		scheduleJobService.resume(jobIds);
		
		return Hdi.ok();
	}

}
