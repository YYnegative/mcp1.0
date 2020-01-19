package com.ebig.hdi.modules.activiti.vo;

import org.activiti.engine.repository.ProcessDefinition;

import java.io.Serializable;

/**
 * 流程实例类
 *
 * @author wenchao
 * @date 2019/09/29
 */
public class ProcessVO implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * key
     */
    private String key;
    /**
     * 部署id
     */
    private String deploymentId;


    public ProcessVO(ProcessDefinition processDefinition) {
        this.setId(processDefinition.getId());
        this.key = processDefinition.getKey();
        this.deploymentId = processDefinition.getDeploymentId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
}
