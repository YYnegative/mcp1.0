package com.ebig.hdi.modules.drugs.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsApprovalEntity;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsEntity;

/**
 * 平台药品信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:25
 */
public interface GoodsPlatformDrugsService extends IService<GoodsPlatformDrugsEntity> {

    void insertByGoodsDrugsApprovalEntity(GoodsPlatformDrugsApprovalEntity goodsPlatformDrugsApprovalEntity);

    void updateByGoodsDrugsApprovalEntity(GoodsPlatformDrugsApprovalEntity goodsPlatformDrugsApprovalEntity);


}

