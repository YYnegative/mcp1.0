package com.ebig.hdi.modules.activiti.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.activiti.entity.ActAssigneeEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

/**
 * @title: ActAssigneeService
 * @projectName mcp
 * @author：wenchao
 * @date：2019-11-12 18:31
 * @version：V1.0
 */
public interface ActAssigneeService extends IService<ActAssigneeEntity> {


     int deleteByNodeId(String nodeId);

     List<ActivityImpl> getActivityList(String deploymentId);

     List<ActivityImpl> selectAllActivity(List<ActivityImpl> activities);

     List<ActAssigneeEntity> selectListByPage(ActAssigneeEntity record);
}
