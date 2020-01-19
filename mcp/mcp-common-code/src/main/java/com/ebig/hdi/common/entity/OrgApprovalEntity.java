package com.ebig.hdi.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 机构变更审批
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-10 21:10:44
 */
@TableName("hdi_org_approval")
public class OrgApprovalEntity implements Serializable {

	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 审批编号
	 */
	private String approvalCode;
	/**
	 * 0.厂商，1.平台，2.医院，3.供应商,4.代理商,5.药店
	 */
	private Integer orgType;
	/**
	 * 变更类型
	 */
	private Integer changeType;
	/**
	 * 提交时间
	 */
	private Date submitTime;
	/**
	 * 提交人id
	 */
	private String userId;
	/**
	 * 提交人
	 */
	private String userName;
	/**
	 * 审批状态
	 */
	private Integer approvalStatus;
	/**
	 * (机构id 医院，供应商，厂商，代理商主键)
	 */
	private Long orgId;
	/**
	 * 流程实例id
	 */
	private String processId;

	/**
	 * 机构编码
	 */
	private String orgCode;

	/**
	 * 机构名称
	 */
	private String orgName;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * 流程id
	 */
	@TableField(exist=false)
	private String taskId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 设置：主键id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：审批编号
	 */
	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}
	/**
	 * 获取：审批编号
	 */
	public String getApprovalCode() {
		return approvalCode;
	}
	/**
	 * 设置：0.厂商，1.平台，2.医院，3.供应商,4.代理商,5.药店
	 */
	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}
	/**
	 * 获取：0.厂商，1.平台，2.医院，3.供应商,4.代理商,5.药店
	 */
	public Integer getOrgType() {
		return orgType;
	}
	/**
	 * 设置：变更类型
	 */
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	/**
	 * 获取：变更类型
	 */
	public Integer getChangeType() {
		return changeType;
	}
	/**
	 * 设置：提交时间
	 */
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	/**
	 * 获取：提交时间
	 */
	public Date getSubmitTime() {
		return submitTime;
	}
	/**
	 * 设置：提交人id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：提交人id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：提交人
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：提交人
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：审批状态
	 */
	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	/**
	 * 获取：审批状态
	 */
	public Integer getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * 设置：(机构id 医院，供应商，厂商，代理商主键)
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	/**
	 * 获取：(机构id 医院，供应商，厂商，代理商主键)
	 */
	public Long getOrgId() {
		return orgId;
	}
	/**
	 * 设置：流程实例id
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	/**
	 * 获取：流程实例id
	 */
	public String getProcessId() {
		return processId;
	}
}
