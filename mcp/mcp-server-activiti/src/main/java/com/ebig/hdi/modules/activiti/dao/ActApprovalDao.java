package com.ebig.hdi.modules.activiti.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ebig.hdi.modules.activiti.entity.ActApprovalEntity;

import java.util.List;
import java.util.Map;

/**
 * 机构变更审批
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-11-10 20:09:55
 */
public interface ActApprovalDao extends BaseMapper<ActApprovalEntity> {

    List<ActApprovalEntity> selectByDeptId(Page<ActApprovalEntity> page, Map<String, Object> params);
}
