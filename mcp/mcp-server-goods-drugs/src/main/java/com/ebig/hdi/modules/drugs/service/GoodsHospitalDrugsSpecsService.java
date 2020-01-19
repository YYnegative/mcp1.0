package com.ebig.hdi.modules.drugs.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.drugs.entity.GoodsHospitalDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsHospitalDrugsEntityVo;

/**
 * 医院药品规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:22:44
 */
public interface GoodsHospitalDrugsSpecsService extends IService<GoodsHospitalDrugsSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(GoodsHospitalDrugsSpecsEntity goodsHospitalDrugsSpecs);

	void insertOrUpdate(List<GoodsHospitalDrugsSpecsEntity> goodsHospitalDrugsSpecsList);

	void save(GoodsHospitalDrugsEntityVo goodsHospitalDrugsEntityVo);

	List<GoodsHospitalDrugsSpecsEntity> selectListByDrugsId(Long drugsId);

}

