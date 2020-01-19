/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.job.task;

import com.ebig.hdi.modules.job.entity.ScheduleJobEntity;

/**
 * 定时任务接口，所有定时任务都要实现该接口
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ITask {

    /**
     * 执行定时任务接口
     *
     * @param jobId   参数，多参数使用JSON数据
     */
    void run(ScheduleJobEntity scheduleJob);
}