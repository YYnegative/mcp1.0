package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

import java.util.List;

/**
 * @title: UnicodeGoodsShipApprovalService
 * @projectName mcp
 * @description: 匹配审批服务接口
 * @author：wenchao
 * @date：2019-12-23 16:22
 * @version：V1.0
 */

public interface UnicodeGoodsShipApprovalService extends IService<UnicodeGoodsShipApprovalEntity> {

    /**
     * 获取count条未匹配的记录
     * @param limit 获取的记录数
     * @return
     */
    List<UnicodeGoodsShipApprovalEntity> selectNotMatch(Integer limit);

    /**
     *
     * 发起审批
     * @param entity 登陆用户信息
     * @param ship
     * @param changeType 变更类型（1.新增 2.更新)
     * @param actTypeEnumKey 审批类型
     * @param activitiConstant 工作流常量
     */
    void initiateApproval(SysUserEntity entity, UnicodeGoodsShipApprovalEntity ship, Integer changeType, Integer actTypeEnumKey, String[] activitiConstant);


    /**
     * 取消已经匹对的医院耗材商品
     * @param shipId
     */
    void cancelMatch(Long shipId,SysUserEntity entity);
}
