package com.ebig.hdi.modules.drugs.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipHistDrugsEntity;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipHistDrugsEntityVo;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
public interface UnicodeGoodsShipHistDrugsDao extends BaseMapper<UnicodeGoodsShipHistDrugsEntity> {
	
	List<UnicodeGoodsShipHistDrugsEntityVo > selectHospitalGoodsHist(Pagination page, Map<String, Object> params);

	List<UnicodeGoodsShipHistDrugsEntityVo > selectSupplierGoodsHist(Pagination page, Map<String, Object> params);

}
