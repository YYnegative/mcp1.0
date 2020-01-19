package com.ebig.hdi.modules.reagent.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.reagent.dao.UnicodeGoodsShipReagentDao;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipReagentEntity;
import com.ebig.hdi.modules.reagent.service.UnicodeGoodsShipService;
import org.springframework.stereotype.Service;


@Service("unicodeGoodsShipReagentService")
public class UnicodeGoodsShipServiceImpl extends ServiceImpl<UnicodeGoodsShipReagentDao, UnicodeGoodsShipReagentEntity> implements UnicodeGoodsShipService {
    @Override
    public void insert(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity) {
        UnicodeGoodsShipReagentEntity unicodeGoodsShipEntity;
        unicodeGoodsShipEntity = ReflectUitls.transform(unicodeGoodsShipApprovalEntity,UnicodeGoodsShipReagentEntity.class);
        this.baseMapper.insert(unicodeGoodsShipEntity);
    }

    @Override
    public void update(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity) {
        UnicodeGoodsShipReagentEntity unicodeGoodsShipEntity;
        unicodeGoodsShipEntity = ReflectUitls.transform(unicodeGoodsShipApprovalEntity,UnicodeGoodsShipReagentEntity.class);
        this.baseMapper.updateById(unicodeGoodsShipEntity);
    }
}
