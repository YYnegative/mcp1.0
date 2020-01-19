package com.ebig.hdi.modules.core.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntity;
import com.ebig.hdi.common.entity.UnicodeGoodsShipHistEntityVo;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-22 09:26:07
 */
public interface UnicodeGoodsShipHistDao extends BaseMapper<UnicodeGoodsShipHistEntity> {
	
	List<UnicodeGoodsShipHistEntityVo> selectHospitalGoodsHist(Pagination page, Map<String, Object> params);

	List<UnicodeGoodsShipHistEntityVo > selectSupplierGoodsHist(Pagination page, Map<String, Object> params);

    List<UnicodeGoodsShipHistEntityVo> selectGoodsHist(Page<UnicodeGoodsShipHistEntityVo> page, Map<String, Object> params);
}
