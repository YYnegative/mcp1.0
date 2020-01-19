package com.ebig.hdi.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 系统用户消息
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-04-26 12:11:51
 */
@Data
@TableName("sys_user_message")
public class SysUserMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 消息id
	 */
	@NotNull
	private Long messageId;
	/**
	 * 阅读者id
	 */
	@NotNull
	private Long userId;
	/**
	 * 所属机构
	 */
	@NotBlank
	private String deptId;
	/**
	 * 状态(0:未读;1:已读;2:删除)
	 */
	@NotNull
	private Integer status;
	/**
	 * 阅读时间
	 */
	private Date readTime;

}
