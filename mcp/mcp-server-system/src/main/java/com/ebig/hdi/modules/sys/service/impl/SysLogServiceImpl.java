/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.sys.dao.SysLogDao;
import com.ebig.hdi.modules.sys.entity.SysLogEntity;
import com.ebig.hdi.modules.sys.service.SysLogService;

@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");

        Page<SysLogEntity> page = this.selectPage(
    			new Query<SysLogEntity>(params).getPage(),
    			new EntityWrapper<SysLogEntity>()
        		.like(StringUtils.isNotBlank(key),"username", key)
        		.or()
        		.like(StringUtils.isNotBlank(key),"operation", key)
        );

        return new PageUtils(page);
    }
}
