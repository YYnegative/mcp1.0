package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalsEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;

import java.util.List;
import java.util.Map;

/**
 * 平台耗材批准文号
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
public interface GoodsPlatformConsumablesApprovalsService extends IService<GoodsPlatformConsumablesApprovalsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<GoodsPlatformConsumablesApprovalsEntity> selectListByConsumablesId(Long consumablesId);

	void saveBatch(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo);

	GoodsPlatformConsumablesApprovalsEntity selectByApprovals(Long consumablesId, String approvals);
}

