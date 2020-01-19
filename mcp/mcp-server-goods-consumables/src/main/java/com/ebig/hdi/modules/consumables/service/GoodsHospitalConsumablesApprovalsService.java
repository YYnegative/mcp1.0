package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsHospitalConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsHospitalConsumablesEntityVo;

import java.util.List;
import java.util.Map;

/**
 * 医院耗材批准文号
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:39:01
 */
public interface GoodsHospitalConsumablesApprovalsService extends IService<GoodsHospitalConsumablesApprovalsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<GoodsHospitalConsumablesApprovalsEntity> selectListByConsumablesId(Long consumablesId);

	void saveBatch(GoodsHospitalConsumablesEntityVo goodsHospitalConsumablesVo);

	GoodsHospitalConsumablesApprovalsEntity selectByApprovals(Long consumablesId, String approvals);
}

