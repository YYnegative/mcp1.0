package com.ebig.hdi.modules.license.dao;

import com.ebig.hdi.modules.license.entity.LicenseClassifyInfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 证照分类信息
 * 
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-21 16:45:19
 */
public interface LicenseClassifyInfoDao extends BaseMapper<LicenseClassifyInfoEntity> {
	Integer selectReferNumber(Long classifyId);
}
