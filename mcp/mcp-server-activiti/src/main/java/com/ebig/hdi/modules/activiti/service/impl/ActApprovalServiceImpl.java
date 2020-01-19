package com.ebig.hdi.modules.activiti.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.enums.ApprovalTypeEnum;
import com.ebig.hdi.common.exception.HdiException;
import com.ebig.hdi.common.utils.Constant;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.activiti.dao.ActApprovalDao;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.activiti.service.ActTaskService;
import com.ebig.hdi.modules.activiti.vo.ApprovalInfoVo;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("actApprovalService")
public class ActApprovalServiceImpl extends ServiceImpl<ActApprovalDao, ActApprovalEntity> implements ActApprovalService {


    @Autowired
    TaskService taskService;

    @Autowired
    private ActTaskService actTaskService;

    private static final String INFO_VOLIST = "approvalInfoVoList";


    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    private static final String NEED_FINNISH = "needfinish";

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params, SysUserEntity user) {
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        // 手动数据过滤
        if (params.get(Constant.SQL_FILTER) != null) {
            String sqlFilter = params.get(Constant.SQL_FILTER).toString();
            params.put("deptIds", sqlFilter);
        }
        Page<ActApprovalEntity> page = new Page<>(currPage, pageSize);
        List<ActApprovalEntity> list = this.baseMapper.selectByDeptId(page, params);
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public ActApprovalEntity getActApprovalEntity(SysUserEntity entity, Integer changeType,
                                                  Integer type, String foreignId,String code,String name) {
        ActApprovalEntity orgApprovalEntity = new ActApprovalEntity();
        orgApprovalEntity.setSubmitTime(new Date());
        orgApprovalEntity.setUserId(entity.getUserId().toString());
        orgApprovalEntity.setUserName(entity.getUsername());
        orgApprovalEntity.setApprovalStatus(ApprovalTypeEnum.WAIT.getKey());
        orgApprovalEntity.setChangeType(changeType);
        orgApprovalEntity.setType(type);
        orgApprovalEntity.setForeignId(foreignId);
        orgApprovalEntity.setCode(code);
        orgApprovalEntity.setName(name);
        return orgApprovalEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer complete(ApprovalInfoVo vo, SysUserEntity user) {

        Task task = taskService.createTaskQuery().processInstanceId(vo.getProcessId()).active().singleResult();
        if (task == null) {
              throw new HdiException("审批流程已经结束,不能进行下一节点审批");
        }
        String taskId = task.getId();
        //审批中
        Integer in = 0;
        Map<String, Object> variables = taskService.getVariables(taskId);
        vo.setCreateTime(new Date());
        vo.setApprovalId(user.getUserId().toString());
        vo.setApprovalName(user.getUsername());
        Map<String, Object> map = new HashMap<>(16);
        map.put("flag", vo.isFlag());

        Object finish = variables.get(NEED_FINNISH);
        if (finish != null && "0".equals(finish)) {
            //结束
            throw new HdiException("审批流程已经结束,不能进行下一节点审批");
        } else {
            if (vo.isFlag()) {
                //通过下一个节点
                map.put(NEED_FINNISH, "1");
                //判断还有没有下一个节点
                TaskDefinition nextTask = getNextTaskInfo(taskId);
                if (nextTask == null) {
                    ActApprovalEntity entity = new ActApprovalEntity();
                    entity.setId(vo.getId());
                    entity.setApprovalStatus(ApprovalTypeEnum.PASS.getKey());
                    this.updateById(entity);
                    //审批通过
                    in = 1;
                }else {
                    in = 0;
                }
            } else {
                //不通过
                map.put(NEED_FINNISH, "0");
                ActApprovalEntity entity = new ActApprovalEntity();
                entity.setId(vo.getId());
                entity.setApprovalStatus(ApprovalTypeEnum.FAIL.getKey());
                this.updateById(entity);
                //审批不通过
                in = 2;
            }
        }
        //审批信息叠加
        List<ApprovalInfoVo> leaveList = new ArrayList<>();
        Object o = variables.get(INFO_VOLIST);
        if (o != null) {
            leaveList = (List<ApprovalInfoVo>) o;
        }
        leaveList.add(vo);
        map.put(INFO_VOLIST, leaveList);
        actTaskService.complete(taskId, "审批", map);
        return in;
    }

    @Override
    public List<ApprovalInfoVo> getList(String processId) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId).singleResult();
        //保证运行ing
        List<ApprovalInfoVo> approvalList = null;
        if (instance != null) {
            Task task = this.taskService.createTaskQuery().processInstanceId(processId).singleResult();
            Map<String, Object> variables = taskService.getVariables(task.getId());
            Object o = variables.get(INFO_VOLIST);
            if (o != null) {
                /*获取历史审核信息*/
                approvalList = (List<ApprovalInfoVo>) o;
            }
        } else {
            approvalList = new ArrayList<>();
            List<HistoricDetail> list = historyService.createHistoricDetailQuery().
                    processInstanceId(processId).list();
            HistoricVariableUpdate variable;
            for (HistoricDetail historicDetail : list) {
                variable = (HistoricVariableUpdate) historicDetail;
                if (INFO_VOLIST.equals(variable.getVariableName())) {
                    approvalList.clear();
                    approvalList.addAll((List<ApprovalInfoVo>) variable.getValue());

                }
            }
        }
        return approvalList;
    }

    @Override
    public ProcessInstance startProcess(String userId, String procDefKey, String title, Map<String, Object> vars) {
        return actTaskService.startProcess(userId, procDefKey, title, vars);
    }

    @Override
    public ActApprovalEntity selectByProcessId(String processId) {
            return this.selectOne(new EntityWrapper<ActApprovalEntity>()
                .eq("process_id", processId));
    }

    /**
     * 获取下一个用户任务信息
     *
     * @param taskId 任务Id信息
     * @return 下一个用户任务用户组信息
     * @throws Exception
     */
    public TaskDefinition getNextTaskInfo(String taskId) {

        ProcessDefinitionEntity processDefinitionEntity;

        String id;

        TaskDefinition task = null;

        //获取流程实例Id信息
        String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();

        //获取流程发布Id信息
        String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();

        processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(definitionId);

        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //当前流程节点Id信息
        String activitiId = execution.getActivityId();

        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();

        //遍历所有节点信息
        for (ActivityImpl activityImpl : activitiList) {
            id = activityImpl.getId();
            if (activitiId.equals(id)) {
                //获取下一个节点信息
                task = nextTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
                break;
            }
        }
        return task;
    }

    /**
     * 下一个任务节点信息,
     * <p>
     * 如果下一个节点为用户任务则直接返回,
     * <p>
     * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
     * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务
     *
     * @param activityImpl      流程节点信息
     * @param activityId        当前流程节点Id信息
     * @param elString          排他网关顺序流线段判断条件
     * @param processInstanceId 流程实例Id信息
     * @return
     */
    private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId) {
        PvmActivity ac;
        Object s;
        // 如果遍历节点为用户任务并且节点不是当前节点信息
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            // 获取该节点下一个节点信息
            return ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
                    .getTaskDefinition();
        } else {
            // 获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                // 获取线路的终点节点
                ac = tr.getDestination();
                // 如果流向线路为排他网关
                if ("exclusiveGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();

                    // 如果网关路线判断条件为空信息
                    if (StringUtils.isEmpty(elString)) {
                        // 获取流程启动时设置的网关判断条件信息
                        elString = getGatewayCondition(ac.getId(), processInstanceId);
                    }

                    // 如果排他网关只有一条线路信息
                    if (outTransitionsTemp.size() == 1) {
                        return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
                                elString, processInstanceId);
                        // 如果排他网关有多条线路信息
                    } else if (outTransitionsTemp.size() > 1) {
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            // 获取排他网关线路判断条件信息
                            s = tr1.getProperty("conditionText");
                            // 判断el表达式是否成立
                            if (isCondition(ac.getId(), StringUtils.trim(s.toString()), elString)) {
                                return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
                                        processInstanceId);
                            }
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                }
            }
            return null;
        }
    }

    /**
     * 查询流程启动时设置排他网关判断条件信息
     *
     * @param gatewayId         排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息
     * @param processInstanceId 流程实例Id信息
     * @return
     */
    public String getGatewayCondition(String gatewayId, String processInstanceId) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        Object object = runtimeService.getVariable(execution.getId(), gatewayId);
        return object == null ? "" : object.toString();
    }

    /**
     * 根据key和value判断el表达式是否通过信息
     *
     * @param key   el表达式key信息
     * @param el    el表达式信息
     * @param value el表达式传入值信息
     * @return
     */
    public boolean isCondition(String key, String el, String value) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }
}
