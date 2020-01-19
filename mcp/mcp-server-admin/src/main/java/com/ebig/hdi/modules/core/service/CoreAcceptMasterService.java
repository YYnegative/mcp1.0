package com.ebig.hdi.modules.core.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreAcceptMasterEntity;
import com.ebig.hdi.modules.sys.entity.SysUserEntity;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
public interface CoreAcceptMasterService extends IService<CoreAcceptMasterEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    PageUtils bedingungenQueryPage(Map<String, Object> params);
    
    void deleteMaster(List<CoreAcceptMasterEntity> listEntity,SysUserEntity user);
    
}

