package com.ebig.hdi.modules.sys.vo;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ebig.hdi.modules.sys.entity.SysUserMessageEntity;

import lombok.Data;

@Data
public class SysUserMessageEntityVo extends SysUserMessageEntity implements Serializable{
	/** 
	 * 描述: TODO <br/>
	 * Fields  serialVersionUID : TODO <br/>
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 消息类型(0:系统通知;1:证照提醒)
	 */
	@NotNull
	private Integer type;
	/**
	 * 标题
	 */
	@NotBlank
	private String title;
	/**
	 * 简介
	 */
	private String description;
	/**
	 * 内容
	 */
	private String content;
	
}