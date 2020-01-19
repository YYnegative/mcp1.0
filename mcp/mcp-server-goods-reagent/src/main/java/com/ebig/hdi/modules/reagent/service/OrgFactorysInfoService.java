package com.ebig.hdi.modules.reagent.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.reagent.entity.OrgFactorysInfoEntity;

/**
 * 厂商信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:40
 */
public interface OrgFactorysInfoService extends IService<OrgFactorysInfoEntity> {
    OrgFactorysInfoEntity selectByName(String factoryName);
}

