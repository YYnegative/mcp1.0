package com.ebig.hdi.modules.activiti.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-13 22:42:54
 */
@Data
@TableName("act_assignee")
public class ActAssigneeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 节点id
	 */
	private String nodeId;
	/**
	 * 办理人
	 */
	private String assignee;
	/**
	 * 角色
	 */
	private String roleId;
	/**
	 * 办理人类型 1办理人 2. 角色
	 */
	private Integer assigneeType;
	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 部署id
	 */
	private String deploymentId;

}
