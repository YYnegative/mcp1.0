package com.ebig.hdi.modules.activiti.controller;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 模型控制器类
 *
 * @author wenchao
 * @date 2019/09/29
 */
@RequestMapping("/activiti")
@RestController
public class ModelController extends AbstractController {
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询
     * @param params
     * @return
     */
    @PostMapping("/model/list")
    public PageUtils list(@RequestBody Map<String, Object> params) {
        Integer pageNo = (Integer) params.get("page");
        Integer pageSize = (Integer) params.get("pageSize");
        String name = (String) params.get("name");
        if (pageNo == null) {
            pageNo = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        List<Model> list;
        if (StringUtils.isNotBlank(name)) {
            list = repositoryService.createModelQuery().modelName(name).listPage(pageNo, pageSize);

        } else {
            list = repositoryService.createModelQuery().listPage(pageNo, pageSize);
        }
        return new PageUtils(list, list.size(), pageSize, pageNo);
    }
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/model/remove")
    public Hdi remove(String id) {
        repositoryService.deleteModel(id);
        return Hdi.ok();
    }

    /**
     * 部署流程
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/model/deploy/{id}")
    public Hdi deploy(String id) {
        //获取模型
        Model modelData = repositoryService.getModel(id);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

        if (bytes == null) {
            return Hdi.error("模型数据为空，请先设计流程并成功保存，再进行发布。");
        }
        try {
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (CollectionUtils.isEmpty(model.getProcesses())) {
                return Hdi.error("数据模型不符要求，请至少设计一条主线流程。");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            //发布流程
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addString(processName, new String(bpmnBytes, "UTF-8"))
                    .deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Hdi.error();
        }
        return Hdi.ok();
    }

    @PostMapping("/model/batchRemove")
    public Hdi batchRemove(String[] ids) {
        for (String id : ids) {
            repositoryService.deleteModel(id);
        }
        return Hdi.ok();
    }
}
