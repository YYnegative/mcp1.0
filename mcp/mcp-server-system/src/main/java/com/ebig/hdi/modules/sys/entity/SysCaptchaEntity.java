/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ebig.hdi.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 系统验证码
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_captcha")
public class SysCaptchaEntity implements Serializable {
    /** 
	 * 描述: TODO <br/>
	 * Fields  serialVersionUID : TODO <br/>
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(type = IdType.INPUT)
    private String uuid;
    /**
     * 验证码
     */
    private String code;
    /**
     * 过期时间
     */
    private Date expireTime;

}
