package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipEntity;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-21 15:18:03
 */
public interface UnicodeGoodsShipService extends IService<UnicodeGoodsShipEntity> {


    void insert(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity);

    void update(UnicodeGoodsShipApprovalEntity unicodeGoodsShipApprovalEntity);

}

