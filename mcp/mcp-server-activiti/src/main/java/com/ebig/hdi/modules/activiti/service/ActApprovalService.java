package com.ebig.hdi.modules.activiti.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.activiti.vo.ApprovalInfoVo;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

/**
 * 机构变更审批
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-10 20:09:55
 */
public interface ActApprovalService extends IService<ActApprovalEntity> {

    PageUtils queryPage(Map<String, Object> params,SysUserEntity user);

    /**
     * 获取一个封装实体 ActApprovalEntity
     * @param entity  当前登陆用户信息
     * @param changeType 变更类型()
     * @param type 类型（0.厂商，1.平台，2.医院，3.供应商,4.代理商,5.药店 6.耗材 7.试剂 8.药品等）
     * @param foreignId ( 医院，供应商，厂商，代理商主键)
     * @param code  编码
     * @param name 名称
     * @return
     */
    ActApprovalEntity getActApprovalEntity(SysUserEntity entity,Integer changeType,
                                           Integer type,String foreignId,String code,String name);

    /**
     * 审批
     * @param vo
     * @param user
     * @return （ 0.待审批,1.通过审批 2.审批不通过）
     */
    Integer complete(ApprovalInfoVo vo, SysUserEntity user);

    /**
     * 获取审批记录
     * @param processId 流程实例id
     * @return
     */
    List<ApprovalInfoVo> getList(String processId);

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

    /**
     * 根据流程实例ID查询
     * @param processId
     * @return
     */
    ActApprovalEntity selectByProcessId(String processId);


}

