package com.ebig.hdi.modules.reagent.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.reagent.dao.GoodsPlatformReagentDao;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentApprovalEntity;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentEntity;
import com.ebig.hdi.modules.reagent.service.GoodsPlatformReagentService;
import org.springframework.stereotype.Service;


@Service("goodsPlatformReagentService")
public class GoodsPlatformReagentServiceImpl extends ServiceImpl<GoodsPlatformReagentDao, GoodsPlatformReagentEntity> implements GoodsPlatformReagentService {

    @Override
    public void insertByFactoryInfoApproval(GoodsPlatformReagentApprovalEntity goodsPlatformReagentApprovalEntity) {
        GoodsPlatformReagentEntity goodsPlatformReagentEntity = new GoodsPlatformReagentEntity();
        goodsPlatformReagentEntity = ReflectUitls.transform(goodsPlatformReagentApprovalEntity,goodsPlatformReagentEntity.getClass());
        this.baseMapper.insert(goodsPlatformReagentEntity);
    }

    @Override
    public void updateByFactoryInfoApproval(GoodsPlatformReagentApprovalEntity goodsPlatformReagentApprovalEntity) {
        GoodsPlatformReagentEntity goodsPlatformReagentEntity = new GoodsPlatformReagentEntity();
        goodsPlatformReagentEntity = ReflectUitls.transform(goodsPlatformReagentApprovalEntity,goodsPlatformReagentEntity.getClass());
        this.baseMapper.updateById(goodsPlatformReagentEntity);
    }
}
