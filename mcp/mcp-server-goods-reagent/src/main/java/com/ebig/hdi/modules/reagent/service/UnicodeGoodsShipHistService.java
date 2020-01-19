package com.ebig.hdi.modules.reagent.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipHistReagentEntity;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
public interface UnicodeGoodsShipHistService extends IService<UnicodeGoodsShipHistReagentEntity> {

    PageUtils queryPageHospital(Map<String, Object> params);
    
    PageUtils queryPageSupplier(Map<String, Object> params);
    
}

