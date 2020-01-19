package com.ebig.hdi.modules.reagent.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.reagent.entity.OrgFactorysInfoEntity;
import org.apache.ibatis.annotations.Param;

public interface OrgFactorysInfoDao extends BaseMapper<OrgFactorysInfoEntity> {
    OrgFactorysInfoEntity selectByName(@Param("factoryName") String factoryName);
}
