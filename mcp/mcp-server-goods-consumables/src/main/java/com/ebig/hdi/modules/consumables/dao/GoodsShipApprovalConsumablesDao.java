package com.ebig.hdi.modules.consumables.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.common.entity.UnicodeGoodsShipApprovalEntity;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author wenchao
 * @email 280310627@qq.com
 * @date 2019-12-06 15:18:03
 */
public interface GoodsShipApprovalConsumablesDao extends BaseMapper<UnicodeGoodsShipApprovalEntity> {
	
	List<UnicodeGoodsShipApprovalEntity> selectHospitaGoods(Pagination page, Map<String, Object> params);
	
	List<UnicodeGoodsShipApprovalEntity> selectSupplierGoods(Pagination page, Map<String, Object> params);

	void updateHospitalPgoodsId(@Param("shipId") Long shipId,@Param("pgoodsId") Long pgoodsId,@Param("pspecsId") Long pspecsId,@Param("papprovalId") Long papprovalId,@Param("editmanid") Long editmanid,@Param("editmanname") String editmanname,@Param("editdate") Timestamp editdate);
	
	void updateSupplierPgoodsId(@Param("shipId") Long shipId,@Param("pgoodsId") Long pgoodsId,@Param("pspecsId") Long pspecsId,@Param("papprovalId") Long papprovalId,@Param("editmanid") Long editmanid,@Param("editmanname") String editmanname,@Param("editdate") Timestamp editdate);

	List<UnicodeGoodsShipApprovalEntity> selectPGoodsList(Pagination page, Map<String, Object> params);

	List<UnicodeGoodsShipApprovalEntity> selectListByColumn(@Param("torgType") Integer torgType, @Param("tgoodsType") Integer tgoodsType, @Param("tgoodsId") Long tgoodsId, @Param("tspecsId") Long tspecsId);
	
	List<UnicodeGoodsShipApprovalEntity> listHospitalMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId, @Param("papprovalId") Long papprovalId);
	
	List<UnicodeGoodsShipApprovalEntity> listSupplierMatchGoods(@Param("torgId") Long torgId, @Param("pgoodsId") Long pgoodsId, @Param("pspecsId") Long pspecsId, @Param("papprovalId") Long papprovalId);
}
