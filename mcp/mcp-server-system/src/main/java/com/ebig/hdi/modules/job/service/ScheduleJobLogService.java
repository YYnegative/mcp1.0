/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.job.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.job.entity.ScheduleJobLogEntity;

/**
 * 定时任务日志
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
}
