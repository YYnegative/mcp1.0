package com.ebig.hdi.modules.reagent.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ebig.hdi.modules.reagent.dao.OrgFactorysInfoDao;
import com.ebig.hdi.modules.reagent.entity.OrgFactorysInfoEntity;
import com.ebig.hdi.modules.reagent.service.OrgFactorysInfoService;

@Service("orgFactorysInfoService")
public class OrgFactorysInfoServiceImpl extends ServiceImpl<OrgFactorysInfoDao, OrgFactorysInfoEntity> implements OrgFactorysInfoService {
    @Override
    public OrgFactorysInfoEntity selectByName(String factoryName) {
        return this.baseMapper.selectByName(factoryName);
    }
}
