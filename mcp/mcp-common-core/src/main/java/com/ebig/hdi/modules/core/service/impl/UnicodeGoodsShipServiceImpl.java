package com.ebig.hdi.modules.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.core.dao.UnicodeGoodsShipDao;
import com.ebig.hdi.modules.core.service.UnicodeGoodsShipService;
import org.springframework.stereotype.Service;


@Service("unicodeGoodsShipService")
public class UnicodeGoodsShipServiceImpl extends ServiceImpl<UnicodeGoodsShipDao, UnicodeGoodsShipEntity> implements UnicodeGoodsShipService {

    @Override
    public void insert(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity) {
        UnicodeGoodsShipEntity unicodeGoodsShipEntity;
        unicodeGoodsShipEntity = ReflectUitls.transform(unicodeGoodsShipApprovalEntity,UnicodeGoodsShipEntity.class);
        this.baseMapper.insert(unicodeGoodsShipEntity);
    }

    @Override
    public void update(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity) {
        UnicodeGoodsShipEntity unicodeGoodsShipEntity;
        unicodeGoodsShipEntity = ReflectUitls.transform(unicodeGoodsShipApprovalEntity,UnicodeGoodsShipEntity.class);
        this.baseMapper.updateById(unicodeGoodsShipEntity);
    }
}
