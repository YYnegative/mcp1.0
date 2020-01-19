package com.ebig.hdi.modules.activiti.service;

import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

/**
 * 任务相关
 */
public interface ActTaskService {
    /**
     * 提交任务, 并保存意见
     *
     * @param taskId    任务ID
     * @param title     流程标题，显示在待办任务标题
     * @param vars      任务变量
     */
    void complete(String taskId, String title, Map<String, Object> vars);

    /**
     * 启动流程
     *
     * @param userId        流程发起人
     * @param procDefKey    流程定义KEY
     * @param title         流程标题
     * @param  vars          流程变量
     * @return 流程实例
     */
    ProcessInstance startProcess(String userId,String procDefKey, String title, Map<String, Object> vars);

}
