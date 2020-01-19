package com.ebig.hdi.modules.reagent.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.reagent.entity.UnicodeGoodsShipReagentEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-21 15:18:03
 */
public interface UnicodeGoodsShipReagentDao extends BaseMapper<UnicodeGoodsShipReagentEntity> {

	
	List<UnicodeGoodsShipReagentEntity> listHospitalMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId);
	
	List<UnicodeGoodsShipReagentEntity> listSupplierMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId);
}
