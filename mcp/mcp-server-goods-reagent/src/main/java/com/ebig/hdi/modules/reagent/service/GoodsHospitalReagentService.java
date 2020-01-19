package com.ebig.hdi.modules.reagent.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.reagent.entity.GoodsHospitalReagentEntity;
import com.ebig.hdi.modules.reagent.vo.GoodsHospitalReagentVO;

/**
 * 医院试剂信息
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:30:58
 */
public interface GoodsHospitalReagentService extends IService<GoodsHospitalReagentEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    void save(GoodsHospitalReagentVO goodsHospitalReagentVO);
    
    void update(GoodsHospitalReagentVO goodsHospitalReagentVO);
    
    GoodsHospitalReagentVO selectById(Long id);

	void toggle(Map<String, Object> params);
	
	GoodsHospitalReagentEntity selectByGoodsNameAndFactoryNameAndHospitalId(String goodsname, String factoryname, Long hospitalId);
	
	//定时任务用	
	void updateIsMatch(Long goodsId);
	
    //HDI转换用
	Map<String, Object> selectBySourcesId(String sourcesSpecsId);

}

