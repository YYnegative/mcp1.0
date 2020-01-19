package com.ebig.hdi.modules.license.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.modules.license.entity.LicenseClassifyInfoEntity;

/**
 * 证照分类信息
 *
 * @author clang
 * @email 690560356@qq.com
 * @date 2019-05-21 16:45:19
 */
public interface LicenseClassifyInfoService extends IService<LicenseClassifyInfoEntity> {

    List<LicenseClassifyInfoEntity> list(Map<String, Object> params);
    
    void save(LicenseClassifyInfoEntity licenseClassifyInfo);

	void delete(Long[] ids);
}

