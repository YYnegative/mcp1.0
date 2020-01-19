package com.ebig.hdi.modules.activiti.service.impl;

import com.ebig.hdi.modules.activiti.service.ActTaskService;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 */
@Service
public class ActTaskServiceImpl implements ActTaskService {
    @Autowired
    TaskService taskService;
    @Autowired
    IdentityService identityService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    FormService formService;

    @Autowired
    RepositoryService repositoryService;


    /**
     * 提交任务, 并保存意见
     *
     * @param taskId    任务ID
     * @param title     流程标题
     * @param vars      任务变量
     */
    @Override
    public void complete(String taskId, String title, Map<String, Object> vars) {
        // 设置流程变量
        if (vars == null) {
            vars = new HashMap<>(16);
        }
        // 设置流程标题
        if (StringUtils.isNotBlank(title)) {
            vars.put("title", title);
        }

        // 提交任务
        taskService.complete(taskId, vars);
    }
    /**
     * 启动流程
     *
     * @param procDefKey 流程定义KEY
     * @param userId     流程发起人
     * @param title      流程标题
     * @param vars       流程变量
     * @return 流程实例
     */
    @Override
    public ProcessInstance startProcess(String userId, String procDefKey, String title, Map<String, Object> vars) {

        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(userId);

        // 设置流程变量
        if (vars == null) {
            vars = new HashMap(16);
        }

        // 设置流程标题
        if (StringUtils.isNotBlank(title)) {
            vars.put("title", title);
        }

        // 启动流程
        return runtimeService.startProcessInstanceByKey(procDefKey, vars);


    }

}

