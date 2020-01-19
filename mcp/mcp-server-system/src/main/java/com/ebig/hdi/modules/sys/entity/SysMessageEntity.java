package com.ebig.hdi.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 系统消息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-04-26 12:11:51
 */
@Data
@TableName("sys_message")
public class SysMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
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
	/**
	 * 状态(0:草稿;1:已发布;2:已删除)
	 */
	@NotNull
	private Integer status;
	/**
	 * 创建人id
	 */
	private Long createId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人id
	 */
	private Long editId;
	/**
	 * 修改时间
	 */
	private Date editTime;
	/**
	 * 发布时间
	 */
	private Date publishTime;

}
