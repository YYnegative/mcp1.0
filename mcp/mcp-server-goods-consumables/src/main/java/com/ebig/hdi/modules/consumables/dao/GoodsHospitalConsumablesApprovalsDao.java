package com.ebig.hdi.modules.consumables.dao;

import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesApprovalsEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 医院耗材批准文号
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:01
 */
public interface GoodsHospitalConsumablesApprovalsDao extends BaseMapper<GoodsHospitalConsumablesApprovalsEntity> {

	List<GoodsHospitalConsumablesApprovalsEntity> selectListByConsumablesId(@Param("consumablesId") Long consumablesId);

	GoodsHospitalConsumablesApprovalsEntity selectByApprovals(@Param("consumablesId") Long consumablesId, @Param("approvals") String approvals);
	
}
