
package com.ebig.hdi.modules.activiti.actlistener;


import com.ebig.hdi.common.enums.AssigneeTypeEnum;
import com.ebig.hdi.common.utils.SpringContextUtils;
import com.ebig.hdi.modules.activiti.entity.ActAssigneeEntity;
import com.ebig.hdi.modules.activiti.service.ActAssigneeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

/**
 * @author wenchao
 * @date 2019/11/11.
 * @email 154040976@qq.com
 * <p>
 * 流程监听器 动态注入节点办理人
 */
public class ActNodeListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        //KEY
        ActAssigneeService actAssigneeService = (ActAssigneeService) SpringContextUtils.getBean("actAssigneeService");
        ActAssigneeEntity entity=  new ActAssigneeEntity();
        entity.setNodeId(delegateTask.getTaskDefinitionKey());
        List<ActAssigneeEntity> assigneeList = actAssigneeService.selectListByPage(entity);
        for (ActAssigneeEntity assignee : assigneeList) {
            Integer assigneeType = assignee.getAssigneeType();
            if(AssigneeTypeEnum.GROUP_TYPE.getKey().equals(assigneeType)){
                delegateTask.addCandidateGroup(assignee.getRoleId());
            }else if (AssigneeTypeEnum.USER_TYPE.getKey().equals(assigneeType)){
                delegateTask.addCandidateUser(assignee.getAssignee());
            }
        }
    }
}
