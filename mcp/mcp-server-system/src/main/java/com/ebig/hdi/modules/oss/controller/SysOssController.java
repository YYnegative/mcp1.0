/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.oss.controller;

import java.io.IOException;
import java.util.Map;

import com.ebig.hdi.common.utils.*;
import com.ebig.hdi.modules.sys.entity.SysDictEntity;
import com.ebig.hdi.modules.sys.service.SysDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ebig.hdi.common.validator.ValidatorUtils;
import com.ebig.hdi.common.validator.group.AliyunGroup;
import com.ebig.hdi.common.validator.group.QcloudGroup;
import com.ebig.hdi.common.validator.group.QiniuGroup;
import com.ebig.hdi.modules.oss.cloud.CloudStorageConfig;
import com.ebig.hdi.modules.oss.service.SysOssService;
import com.ebig.hdi.modules.sys.service.SysConfigService;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;

	@Autowired
	private SysDictService sysDictService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
	/**
	 * 列表
	 */
	@GetMapping("/list")
	//@RequiresPermissions("sys:oss:all")
	public Hdi list(@RequestParam Map<String, Object> params){
		PageUtils page = sysOssService.queryPage(params);

		return Hdi.ok().put("page", page);
	}


    /**
     * 云存储配置信息
     */
    @GetMapping("/config")
    @RequiresPermissions("sys:oss:all")
    public Hdi config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return Hdi.ok().put("config", config);
    }


	/**
	 * 保存云存储配置信息
	 */
	@PostMapping("/saveConfig")
	@RequiresPermissions("sys:oss:all")
	public Hdi saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return Hdi.ok();
	}
	

	/**
	 * 上传文件
	 */
	/*@PostMapping("/upload")
	@RequiresPermissions("sys:oss:all")
	public Hdi upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new HdiException("上传文件不能为空");
		}

		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.insert(ossEntity);

		return Hdi.ok().put("url", url);
	}*/
	
	/**
	 * 调用fastdfs客户端上传文件
	 * @param file 
	 * @return 上传成功后的文件URL
	 * @throws Exception
	 */
	@PostMapping("/upload")
	//@RequiresPermissions("sys:oss:all")
    public Hdi upload(@RequestParam("file") MultipartFile file) throws Exception {
		String fileUrl = null;
		try{
			fileUrl = sysOssService.uploadFile(file);
		}catch (Exception e) {
			return Hdi.error("上传文件失败");
		}
        return Hdi.ok().put("url", fileUrl);
    }
	
	/**
	 * 调用fastdfs客户端删除文件
	 * @param fileName 文件url
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/delete")
    //@RequiresPermissions("sys:oss:all")
    public Hdi delete(@RequestBody Long[] ids) throws Exception{
    	sysOssService.deleteFile(ids);
    	return Hdi.ok();
    }


	/**
	 * 删除
	 */
	/*@PostMapping("/delete")
	@RequiresPermissions("sys:oss:all")
	public Hdi delete(@RequestBody Long[] ids){
		sysOssService.deleteBatchIds(Arrays.asList(ids));

		return Hdi.ok();
	}*/
}
