package com.ebig.hdi.modules.reagent.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentApprovalEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentEntity;

/**
 * 平台试剂信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
public interface GoodsPlatformReagentService extends IService<GoodsPlatformReagentEntity> {

    void insertByFactoryInfoApproval(GoodsPlatformReagentApprovalEntity goodsPlatformReagentApprovalEntity);

    void updateByFactoryInfoApproval(GoodsPlatformReagentApprovalEntity goodsPlatformReagentApprovalEntity);
}

