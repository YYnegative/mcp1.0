package com.ebig.hdi.modules.reagent.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipHistReagentEntity;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipHistReagentEntityVo;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
public interface UnicodeGoodsShipHistReagentDao extends BaseMapper<UnicodeGoodsShipHistReagentEntity> {
	
	List<UnicodeGoodsShipHistReagentEntityVo > selectHospitalGoodsHist(Pagination page, Map<String, Object> params);

	List<UnicodeGoodsShipHistReagentEntityVo > selectSupplierGoodsHist(Pagination page, Map<String, Object> params);

}
