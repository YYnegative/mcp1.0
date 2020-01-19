package com.ebig.hdi.modules.consumables.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesApprovalEntity;
import com.ebig.hdi.modules.consumables.entity.GoodsPlatformConsumablesEntity;
import com.ebig.hdi.modules.consumables.vo.GoodsPlatformConsumablesEntityVo;

/**
 * 平台耗材信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:37:46
 */
public interface GoodsPlatformConsumablesService extends IService<GoodsPlatformConsumablesEntity> {

    void insertByFactoryInfoApproval(GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumablesApprovalEntity);

    void updateByFactoryInfoApproval(GoodsPlatformConsumablesApprovalEntity goodsPlatformConsumablesApprovalEntity);

    void updateByGoodsDrugsApprovalEntity(GoodsPlatformConsumablesEntityVo goodsPlatformConsumablesVo);
}

