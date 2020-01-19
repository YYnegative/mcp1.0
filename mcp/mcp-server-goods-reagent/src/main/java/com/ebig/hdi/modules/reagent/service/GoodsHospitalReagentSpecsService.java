package com.ebig.hdi.modules.reagent.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentSpecsEntity;
import com.ebig.hdi.modules.reagent.vo.GoodsHospitalReagentVO;

/**
 * 医院试剂规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
public interface GoodsHospitalReagentSpecsService extends IService<GoodsHospitalReagentSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<GoodsHospitalReagentSpecsEntity> selectListByReagentId(Long id);

	void save(GoodsHospitalReagentVO goodsHospitalReagentVO);

	void insertOrUpdate(List<GoodsHospitalReagentSpecsEntity> goodsHospitalReagentSpecsList);
}

