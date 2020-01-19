package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreAcceptDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
public interface CoreAcceptDetailService extends IService<CoreAcceptDetailEntity> {

	PageUtils queryPageDetail(Map<String, Object> params);

	Integer getAcceptNumberBySupplyMasterId(Long supplyMasterId);
}

