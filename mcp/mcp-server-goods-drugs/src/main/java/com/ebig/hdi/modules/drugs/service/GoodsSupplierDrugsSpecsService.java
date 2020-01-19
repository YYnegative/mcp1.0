package com.ebig.hdi.modules.drugs.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.drugs.entity.GoodsSupplierDrugsSpecsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsSupplierDrugsEntityVo;

import java.util.List;
import java.util.Map;

/**
 * 供应商药品规格
 *
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:17:50
 */
public interface GoodsSupplierDrugsSpecsService extends IService<GoodsSupplierDrugsSpecsEntity> {

    PageUtils queryPage(Map<String, Object> params);

	void save(GoodsSupplierDrugsEntityVo goodsSupplierDrugsEntityVo);

	void save(GoodsSupplierDrugsSpecsEntity goodsSupplierDrugsSpecs);

	void insertOrUpdate(List<GoodsSupplierDrugsSpecsEntity> goodsSupplierDrugsSpecsList);

	List<GoodsSupplierDrugsSpecsEntity> selectListByDrugsId(Long drugsId);
}

