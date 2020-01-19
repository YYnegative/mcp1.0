package com.ebig.hdi.modules.activiti.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: ApprovalInfoVo
 * @projectName mcp
 * @description: 审批信息实体类
 * @author：wenchao
 * @date：2019-11-10 21:57
 * @version：V1.0
 */
@Data
public class ApprovalInfoVo implements Serializable{

    private Long id;
    /**
     * 审批人id
     */
    private String approvalId;

    /**
     * 审批人姓名
     */
    private String approvalName;
    /**
     * 审批意见
     */
    private String opinion;
    /**
     * 审批时间
     */
    private Date createTime;
    /**
     * 是否通过
     */
    private boolean flag;
    /**
     * 流程id
     */
    private String processId;

    /**
     * 角色名称
     */
    private String roleName;
}
