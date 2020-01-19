/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.oss.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.ebig.hdi.modules.oss.entity.SysOssEntity;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysOssDao extends BaseMapper<SysOssEntity> {
	
}
