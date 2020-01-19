package com.ebig.hdi.modules.consumables.dao;

import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 平台耗材批准文号
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
public interface GoodsPlatformConsumablesApprovalsDao extends BaseMapper<GoodsPlatformConsumablesApprovalsEntity> {

	GoodsPlatformConsumablesApprovalsEntity selectByApprovals(@Param("consumablesId") Long consumablesId, @Param("approvals") String approvals);

	List<GoodsPlatformConsumablesApprovalsEntity> selectListByConsumablesId(@Param("consumablesId") Long consumablesId);

    List<String> selectAllApprovals();

}
