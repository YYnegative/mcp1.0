package com.ebig.hdi.modules.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @title: NodeVo
 * @projectName mcp
 * @description: 流程节点
 * @author：wenchao
 * @date：2019-11-13 22:34
 * @version：V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeVo implements Serializable {

    /**
     * 节点角色id
     */
    private Long roleId;

    /**
     * 节点角色名称
     */
    private String roleName;


}
