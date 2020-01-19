package com.ebig.hdi.modules.drugs.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.drugs.entity.UnicodeGoodsShipDrugsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-21 15:18:03
 */
public interface UnicodeGoodsShipDrugsDao extends BaseMapper<UnicodeGoodsShipDrugsEntity> {

	List<UnicodeGoodsShipDrugsEntity> listHospitalMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId);
	
	List<UnicodeGoodsShipDrugsEntity> listSupplierMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId);
}
