package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
public interface UnicodeGoodsShipHistService extends IService<UnicodeGoodsShipHistEntity> {

    PageUtils queryPageHospital(Map<String, Object> params);
    
    PageUtils queryPageSupplier(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params);
}

