package com.ebig.hdi.modules.activiti.controller;

import com.ebig.hdi.common.utils.Hdi;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.activiti.service.ActApprovalService;
import com.ebig.hdi.modules.activiti.vo.ActApprovalVo;
import com.ebig.hdi.modules.activiti.vo.ApprovalInfoVo;
import com.ebig.hdi.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 机构变更审批
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-10 20:09:55
 */
@RestController
@RequestMapping("activiti/approval")
public class ActApprovalController extends AbstractController {
    @Autowired
    private ActApprovalService actApprovalService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Hdi list(@RequestBody Map<String, Object> params) {
        params.put("deptId", getDeptId());
        PageUtils page = actApprovalService.queryPage(params, getUser());

        return Hdi.ok().put("page", page);
    }
    @RequestMapping("/info/{processId}")
    public Hdi info(@PathVariable("processId") String processId){
        ActApprovalEntity actApprovalEntity = actApprovalService.selectByProcessId(processId);
        ActApprovalVo vo = ReflectUitls.transform(actApprovalEntity,ActApprovalVo.class);
        return Hdi.ok().put("entity", vo);
    }

    @PostMapping("/complete")
    public Hdi complete(@RequestBody ApprovalInfoVo vo) {
        Integer i = -1;
        try {
             i = actApprovalService.complete(vo, getUser());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Hdi.error(e.getMessage());
        }
        return Hdi.ok().put("status", i);
    }

    /**
     * 根据 执行对象id获取审批信息
     *
     * @param processId
     * @return
     */
    @PostMapping("/approvaldetail")
    public Hdi approvalDetail(@RequestBody String processId) {
        List<ApprovalInfoVo> approvalList = actApprovalService.getList(processId);
        return Hdi.ok().put("list", approvalList);
    }
}
