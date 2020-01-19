package com.ebig.hdi.modules.goods.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.modules.goods.service.TempPubSupplyGoodsService;
import com.ebig.hdi.modules.job.dao.TempPubSupplyGoodsDao;
import com.ebig.hdi.modules.job.entity.TempPubSupplyGoodsEntity;
import org.springframework.stereotype.Service;

@Service("tempPubSupplyGoodsService")
public class TempPubSupplyGoodsServiceImpl extends ServiceImpl<TempPubSupplyGoodsDao, TempPubSupplyGoodsEntity> implements TempPubSupplyGoodsService {
}
