package com.ebig.hdi.modules.activiti.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: ActApprovalVo
 * @projectName mcp
 * @description: ${todo}
 * @author：wenchao
 * @date：2019-12-30 21:01
 * @version：V1.0
 */
@Data
public class ActApprovalVo implements Serializable {
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
}
