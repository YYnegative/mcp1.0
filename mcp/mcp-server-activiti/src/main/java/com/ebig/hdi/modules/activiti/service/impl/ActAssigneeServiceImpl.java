package com.ebig.hdi.modules.activiti.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.modules.activiti.dao.ActAssigneeDao;
import com.ebig.hdi.modules.activiti.entity.ActAssigneeEntity;
import com.ebig.hdi.modules.activiti.service.ActAssigneeService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenchao
 * @date 2019/11/11.
 * @email 154040976@qq.com
 */
@Service("actAssigneeService")
public class ActAssigneeServiceImpl  extends ServiceImpl<ActAssigneeDao, ActAssigneeEntity> implements ActAssigneeService {


    @Autowired
    private RepositoryService repositoryService;


    @Override
    public int deleteByNodeId(String nodeId) {
        return this.baseMapper.deleteByNodeId(nodeId);
    }

    @Override
    public List<ActivityImpl> getActivityList(String deploymentId) {

        org.activiti.engine.repository.ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        if(processDefinition !=null){
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinition.getId());
            return selectAllActivity(processDefinitionEntity.getActivities());
        }
        return new ArrayList<>();

    }

    @Override
    public List<ActivityImpl> selectAllActivity(List<ActivityImpl> activities) {
        List<ActivityImpl> list = new ArrayList<>(activities);
        for (ActivityImpl activity : activities) {
            List<ActivityImpl> childActivities = activity.getActivities();
            if (!childActivities.isEmpty()) {
                list.addAll(selectAllActivity(childActivities));
            }
        }
        return list;
    }

    @Override
    public List<ActAssigneeEntity> selectListByPage(ActAssigneeEntity record) {
        return this.baseMapper.selectListByPage(record);
    }
}
