package com.ebig.hdi.modules.license.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.license.entity.LicenseAgentInfoEntity;
import com.ebig.hdi.modules.license.entity.vo.LicenseAgentInfoVO;

/**
 * 代理商证照信息
 *
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-05-21 17:26:29
 */
public interface LicenseAgentInfoService extends IService<LicenseAgentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    LicenseAgentInfoVO selectByAgentId(Long id);
    
    void save(LicenseAgentInfoVO licenseAgentInfoEntityVO);
    
    void update(LicenseAgentInfoVO licenseAgentInfoVO);
    
    void replace(LicenseAgentInfoVO licenseAgentInfoVO);
}

