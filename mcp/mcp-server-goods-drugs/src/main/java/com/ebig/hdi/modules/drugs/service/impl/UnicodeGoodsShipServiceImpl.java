package com.ebig.hdi.modules.drugs.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;
import com.ebig.hdi.common.utils.ReflectUitls;
import com.ebig.hdi.modules.drugs.dao.UnicodeGoodsShipDrugsDao;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipDrugsEntity;
import com.ebig.hdi.modules.drugs.service.UnicodeGoodsShipService;
import org.springframework.stereotype.Service;


@Service("unicodeGoodsShipDrugsService")
public class UnicodeGoodsShipServiceImpl extends ServiceImpl<UnicodeGoodsShipDrugsDao, UnicodeGoodsShipDrugsEntity> implements UnicodeGoodsShipService {


    @Override
    public void insert(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity) {
        UnicodeGoodsShipDrugsEntity unicodeGoodsShipEntity;
        unicodeGoodsShipEntity = ReflectUitls.transform(unicodeGoodsShipApprovalEntity,UnicodeGoodsShipDrugsEntity.class);
        this.baseMapper.insert(unicodeGoodsShipEntity);
    }

    @Override
    public void update(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity) {
        UnicodeGoodsShipDrugsEntity unicodeGoodsShipEntity;
        unicodeGoodsShipEntity = ReflectUitls.transform(unicodeGoodsShipApprovalEntity,UnicodeGoodsShipDrugsEntity.class);
        this.baseMapper.updateById(unicodeGoodsShipEntity);
    }

}
