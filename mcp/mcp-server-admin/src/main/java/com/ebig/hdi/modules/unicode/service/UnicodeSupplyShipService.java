package com.ebig.hdi.modules.unicode.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.unicode.entity.UnicodeSupplyShipEntity;

/**
 * 
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-17 10:56:27
 */
public interface UnicodeSupplyShipService extends IService<UnicodeSupplyShipEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void save(UnicodeSupplyShipEntity unicodeSupplyShipEntity);
    
    PageUtils matching(Map<String, Object> params);
    
    void update(UnicodeSupplyShipEntity unicodeSupplyShipEntity);

    /**
     * 查询
     * @param supplierId 平台供应商id
     * @param hospitalId  平台医院id
     * @return
     */
    UnicodeSupplyShipEntity selectBySupplierIdAndHospitalId(Long supplierId,Long hospitalId);
    
}

