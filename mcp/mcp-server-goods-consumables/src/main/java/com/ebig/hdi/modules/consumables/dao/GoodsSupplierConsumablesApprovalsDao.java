package com.ebig.hdi.modules.consumables.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.consumables.entity.GoodsSupplierConsumablesApprovalsEntity;

/**
 * 供应商耗材批准文号
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:36:02
 */
public interface GoodsSupplierConsumablesApprovalsDao extends BaseMapper<GoodsSupplierConsumablesApprovalsEntity> {
	
	List<GoodsSupplierConsumablesApprovalsEntity> selectByConsumablesId(@Param("id")Long id);
	
	GoodsSupplierConsumablesApprovalsEntity selectByApprovals(String approvals);
}
