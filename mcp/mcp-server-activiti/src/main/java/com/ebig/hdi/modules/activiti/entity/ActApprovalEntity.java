package com.ebig.hdi.modules.activiti.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 变更审批
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-14 14:57:23
 */
@TableName("hdi_act_approval")
public class ActApprovalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 *  0.厂商，1.平台，2.医院，3.供应商,4.代理商,5.药店 6.耗材 7.试剂 8.药品
	 */
	private Integer type;
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
	 * ( 医院，供应商，厂商，代理商，商品等主键)
	 */
	private String foreignId;
	/**
	 * 流程实例id
	 */
	private String processId;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 名称
	 */
	private String name;

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
	 * 设置： 0.厂商，1.平台，2.医院，3.供应商,4.代理商,5.药店 6.耗材 7.试剂 8.药品
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取： 0.厂商，1.平台，2.医院，3.供应商,4.代理商,5.药店 6.耗材 7.试剂 8.药品
	 */
	public Integer getType() {
		return type;
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
	 * 设置：( 医院，供应商，厂商，代理商，商品等主键)
	 */
	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}
	/**
	 * 获取：( 医院，供应商，厂商，代理商，商品等主键)
	 */
	public String getForeignId() {
		return foreignId;
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
	/**
	 * 设置：编码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：编码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}

}
