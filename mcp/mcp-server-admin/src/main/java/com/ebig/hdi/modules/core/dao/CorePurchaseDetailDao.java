package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CorePurchaseDetailEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-27 11:13:13
 */
public interface CorePurchaseDetailDao extends BaseMapper<CorePurchaseDetailEntity> {
	
	List<CorePurchaseDetailEntity> selectByPurchaseMasterId(Pagination page, Map<String, Object> params); 
	
	List<CorePurchaseDetailEntity> selectByMasterid(@Param("purchaseMasterId") Long purchaseMasterId);
	
	List<CorePurchaseDetailEntity> selectByMasterId(@Param("purchaseMasterId") Long purchaseMasterId);
	
	//List<CorePurchaseDetailEntity> selectGoodsName(@Param("purchaseMasterId") Long purchaseMasterId);
	
	//List<CorePurchaseDetailEntity> selectHgoodsName(@Param("purchaseMasterId") Long purchaseMasterId);
	
	List<CorePurchaseDetailEntity> queryDetails(@Param("purchaseMasterId") Long purchaseMasterId, @Param("purplanno") String purplanno, @Param("supplierId") Long supplierId);
	
	List<CorePurchaseDetailEntity> selectSupplierGoods(@Param("hgoodsid") Long hgoodsid,@Param("goodsclass") Integer goodsclass,@Param("hgoodstype") String hgoodstype);
	
	Map<String,Object> selectYHospital(@Param("goodsclass") Integer goodsclass,@Param("hgoodsid") Long hgoodsid,@Param("hgoodstypeid") Long hgoodstypeid);	
	
	List<Map<String,Object>> selectShipFlag(@Param("purchaseMasterId") Long purchaseMasterId);
	
	Integer selectDetailLeaveSupplyQty(@Param("purchaseDetailId") Long purchaseDetailId);

	Integer getPurchaseNumberByPurchaseMasterId(@Param("purchaseMasterId") Long purchaseMasterId);

	CorePurchaseDetailEntity selectByOrgdatadtlid(@Param("orgdatadtlid") String orgdatadtlid);

	Map<String,Object> selectViewByGoodsNameAndSpecs(@Param("hospitalId")Long hospitalId,@Param("hgoodsname")String hgoodsname,@Param("hgoodstype") String hgoodstype,@Param("factoryName") String factoryName);
}
