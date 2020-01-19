package com.ebig.hdi.modules.activiti.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.activiti.entity.ActAssigneeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: ActAssigneeDao
 * @projectName mcp
 * @description:
 * @author：wenchao
 * @date：2019-11-11 10:56
 * @version：V1.0
 */
public interface ActAssigneeDao  extends BaseMapper<ActAssigneeEntity> {

    int deleteByNodeId(@Param("nodeId") String nodeId);

    List<ActAssigneeEntity> selectListByPage(ActAssigneeEntity record);
}
