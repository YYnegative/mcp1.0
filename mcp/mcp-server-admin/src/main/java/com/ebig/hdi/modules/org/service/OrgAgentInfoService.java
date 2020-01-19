package com.ebig.hdi.modules.org.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.org.entity.OrgAgentInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 代理商信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-04-23 09:27:41
 */
public interface OrgAgentInfoService extends IService<OrgAgentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void save(OrgAgentInfoEntity orgAgentInfoEntity);
    
    void update(OrgAgentInfoEntity orgAgentInfoEntity);

	void delete(Long[] ids);

	List<OrgAgentInfoEntity> queryByAgentName(Map<String, Object> params);
}

