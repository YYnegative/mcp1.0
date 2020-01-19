/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.oss.service;
import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.oss.entity.SysOssEntity;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	String uploadFile(MultipartFile file) throws IOException;
	
	void deleteFile(Long[] ids);
}
