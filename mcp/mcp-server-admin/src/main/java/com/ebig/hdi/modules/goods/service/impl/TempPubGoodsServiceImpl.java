package com.ebig.hdi.modules.goods.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.modules.goods.service.TempPubGoodsService;
import com.ebig.hdi.modules.job.dao.TempPubGoodsDao;
import com.ebig.hdi.modules.job.entity.TempPubGoodsEntity;
import org.springframework.stereotype.Service;

/**
 * Created by wen on 2019/8/22.
 */
@Service("tempPubGoodsService")
public class TempPubGoodsServiceImpl extends ServiceImpl<TempPubGoodsDao, TempPubGoodsEntity> implements TempPubGoodsService {
}
