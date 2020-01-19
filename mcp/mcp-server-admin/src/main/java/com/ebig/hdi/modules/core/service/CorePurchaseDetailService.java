package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 
 *
 * @author frinks
 * @email 
 * @date 2019-05-27 11:13:13
 */
public interface CorePurchaseDetailService extends IService<CorePurchaseDetailEntity> {

	PageUtils queryPageDetail(Map<String, Object> params);
	
	//HDI转化用   增加采购细单
	void saveCorePurchaseDetail(CorePurchaseDetailEntity entity);
	
	/**
	 * 查询采购细单剩余供货数量
	 * @param purchaseDetailId
	 * @return
	 */
	Integer selectDetailLeaveSupplyQty(@Param("purchaseDetailId") Long purchaseDetailId);

	List<CorePurchaseDetailEntity> queryDetails(Long purchaseMasterId, String purplanno, Long supplierId);
}

