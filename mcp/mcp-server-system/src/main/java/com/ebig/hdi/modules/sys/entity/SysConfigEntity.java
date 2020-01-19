/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 系统配置信息
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_config")
public class SysConfigEntity implements Serializable {
	/** 
	 * 描述: TODO <br/>
	 * Fields  serialVersionUID : TODO <br/>
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId
	private Long id;
	@NotBlank(message="参数名不能为空")
	private String paramKey;
	@NotBlank(message="参数值不能为空")
	private String paramValue;
	private String remark;

}
