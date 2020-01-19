package com.ebig.hdi.modules.activiti.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.activiti.entity.ActAssigneeEntity;
import com.ebig.hdi.modules.activiti.service.ActAssigneeService;
import com.ebig.hdi.modules.activiti.service.ProcessService;
import com.ebig.hdi.modules.activiti.vo.NodeVo;
import com.ebig.hdi.modules.activiti.vo.ProcessVO;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.ebig.hdi.modules.sys.entity.SysRoleEntity;
import com.ebig.hdi.modules.sys.service.SysRoleService;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程控制器类
 *
 * @author wenchao
 * @date 2019/09/29
 */
@RequestMapping("activiti/process")
@RestController
public class ProcessController extends AbstractController {

    private static final String ZIP = "zip";

    private static final String BAR = "bar";

    private static final String PNG = "png";

    private static final String BPMN = "bpmn";
    private static final String BPMN20 = "bpmn20.xml";
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ActAssigneeService actAssigneeService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @PostMapping("/list")
    PageUtils list(@RequestBody Map<String, Object> params) {
        Integer pageNo = (Integer) params.get("page");
        Integer pageSize = (Integer) params.get("limit");
        if (pageNo == null) {
            pageNo = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .listPage(pageNo, pageSize);
        List<Object> list = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitions) {
            list.add(new ProcessVO(processDefinition));
        }
        return new PageUtils(list, processDefinitions.size(), pageSize, pageNo);
    }

    /**
     * 保存
     *
     * @param category
     * @param file
     * @return
     */
    @PostMapping("/save")
    public Hdi save(String category, MultipartFile file) {
        StringBuilder message = new StringBuilder();
        String fileName = file.getOriginalFilename();
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;
            String extension = FilenameUtils.getExtension(fileName);
            if (ZIP.equals(extension) || BAR.equals(extension)) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else if (PNG.equals(extension)) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (fileName.indexOf(BPMN20) != -1) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (BPMN.equals(extension)) {
                // bpmn扩展名特殊处理，转换为bpmn20.xml
                String baseName = FilenameUtils.getBaseName(fileName);
                deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
            } else {
                message.append("不支持的文件类型：" + extension);
            }
            if (null != deployment) {
                List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
                // 设置流程分类
                for (ProcessDefinition processDefinition : list) {
                    repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
                    //预设一个角色
                    List<ActivityImpl> activityList = actAssigneeService.getActivityList(deployment.getId());
                    if (CollectionUtils.isNotEmpty(activityList)) {
                        for (ActivityImpl act : activityList) {
                            String type = (String) act.getProperty("type");
                            if (StringUtils.isEmpty(type) || "startEvent".equals(type) || "endEvent".equals(type)) {
                                continue;
                            }
                            String name = (String) act.getProperty("name");
                            List<SysRoleEntity> roles = roleService.selectList(new EntityWrapper<SysRoleEntity>().eq("role_name", name));
                            ActAssigneeEntity assigneeEntity = new ActAssigneeEntity();
                            if (CollectionUtils.isEmpty(roles)) {
                                SysRoleEntity e = new SysRoleEntity();
                                e.setRoleName(name);
                                e.setCreateTime(new Date());
                                e.setDeptId(getDeptId());
                                e.setRemark(name);
                                roleService.insert(e);
                                assigneeEntity.setRoleId(e.getRoleId().toString());
                            }
                            assigneeEntity.setNodeId(act.getId());
                            assigneeEntity.setRoleId(roles.get(0).getRoleId().toString());
                            assigneeEntity.setAssigneeType(2);
                            assigneeEntity.setDeploymentId(deployment.getId());
                            assigneeEntity.setNodeName(name);
                            actAssigneeService.insert(assigneeEntity);
                        }
                    }

                    message.append("部署成功，流程ID=" + processDefinition.getId());
                }

                if (CollectionUtils.isEmpty(list)) {
                    message.append("部署失败，没有流程。");
                }
            }

        } catch (Exception e) {
            throw new ActivitiException("部署失败！", e);
        }
        return Hdi.ok(message.toString());
    }
    @GetMapping("/noderole/{processId}")
    public Hdi getNodeRole(@PathVariable("processId") String processId) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        List<NodeVo> list = new ArrayList<>();
        if (instance != null) {
            List<ActivityImpl> activityList = actAssigneeService.getActivityList(instance.getDeploymentId());
            if (CollectionUtils.isNotEmpty(activityList)) {
                for (ActivityImpl act : activityList) {
                    String type = (String) act.getProperty("type");
                    if (StringUtils.isEmpty(type) || "startEvent".equals(type) || "endEvent".equals(type)) {
                        continue;
                    }
                    //节点id 、name
                    NodeVo vo = new NodeVo();
                    vo.setRoleName((String) act.getProperty("name"));
                    ActAssigneeEntity entity = actAssigneeService.selectOne( new EntityWrapper<ActAssigneeEntity>()
                            .eq("node_id", act.getId())
                            .eq("deployment_id", instance.getDeploymentId()));
                    if(entity != null){
                        vo.setRoleId(Long.parseLong(entity.getRoleId()));
                    }
                    list.add(vo);
                }
            }
        } else {
            List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(processId).list();
            if (CollectionUtils.isNotEmpty(tasks)) {
                for (HistoricTaskInstance task : tasks) {
                    NodeVo vo = new NodeVo();
                    vo.setRoleName(task.getName());
                    list.add(vo);
                }
            }
        }
        return Hdi.ok().put("list", list);
    }

    @PostMapping("/remove")
    public Hdi remove(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return Hdi.ok();
    }

    @PostMapping("/batchRemove")
    public Hdi batchRemove(String[] deploymentIds) {
        for (String id : deploymentIds) {
            repositoryService.deleteDeployment(id, true);
        }
        return Hdi.ok();
    }
}
