/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.oss.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.annotation.DataFilter;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.FastDFSClientUtils;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.Query;
import com.ebig.hdi.modules.oss.dao.SysOssDao;
import com.ebig.hdi.modules.oss.entity.SysOssEntity;
import com.ebig.hdi.modules.oss.service.SysOssService;



@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {
	
	@Autowired
    private FastDFSClientUtils fastDFSClientUtils;
	
	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		Page<SysOssEntity> page = this.selectPage(
    			new Query<SysOssEntity>(params).getPage(),
    			new EntityWrapper<SysOssEntity>()
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
		);

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String uploadFile(MultipartFile file) throws IOException  {
		String fileUrl =  fastDFSClientUtils.uploadFile(file);
    	//保存文件信息
    	SysOssEntity ossEntity = new SysOssEntity();
    	ossEntity.setUrl(fileUrl);
    	ossEntity.setCreateDate(new Date());
    	baseMapper.insert(ossEntity);
		return fileUrl;
	}

	@Override
	public void deleteFile(Long[] ids) {
		if( ids != null ){
			for(Long id : ids){
				SysOssEntity entity = baseMapper.selectById(id);
				fastDFSClientUtils.deleteFile(entity.getUrl());
				baseMapper.deleteById(id);
			}
		}
		
	}
	
}
