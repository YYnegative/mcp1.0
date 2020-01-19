/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.job.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.job.dao.ScheduleJobLogDao;
import com.ebig.hdi.modules.job.entity.ScheduleJobLogEntity;
import com.ebig.hdi.modules.job.service.ScheduleJobLogService;


@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String jobId = (String)params.get("jobId");

		Page<ScheduleJobLogEntity> page = this.selectPage(
    			new Query<ScheduleJobLogEntity>(params).getPage(),
    			new EntityWrapper<ScheduleJobLogEntity>()
			.like(StringUtils.isNotBlank(jobId),"job_id", jobId)
		);

		return new PageUtils(page);
	}

}
