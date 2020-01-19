package com.ebig.hdi.modules.reagent.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.vo.GoodsPlatformReagentVO;

/**
 * 平台试剂规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:28:15
 */
public interface GoodsPlatformReagentSpecsService extends IService<GoodsPlatformReagentSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
	void insertOrUpdate(List<GoodsPlatformReagentSpecsEntity> goodsPlatformReagentSpecsList);

	void save(GoodsPlatformReagentVO goodsPlatformReagentVO);

	List<GoodsPlatformReagentSpecsEntity> selectListByReagentId(Long reagentId);

	GoodsPlatformReagentSpecsEntity selectByReagentIdAndSpecs(Long id, String specs);
    
}

