package com.ebig.hdi.modules.core.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author frink
 * @email
 * @date 2019-05-25 12:01:14
 */
public interface CoreSupplyDetailDao extends BaseMapper<CoreSupplyDetailEntity> {

	List<CoreSupplyDetailEntity> selectBysupplyMasterId(Pagination page, Map<String, Object> params);

	List<CoreSupplyDetailEntity> selectSupplyDetail(@Param("supplyMasterId") Long supplyMasterId);

	List<Map<String, Object>> selectLot(@Param("goodsid") Long goodsid, @Param("goodsclass") Integer goodsclass, @Param("goodstypeid") Long goodstypeid, @Param("lotno") String lotno);

	//提交医院
	List<CoreSupplyDetailEntity> selectBySupplyMasterId(@Param("supplyMasterId") Long supplyMasterId);

	CoreSupplyDetailEntity selectByDetailids(@Param("supplyDetailId") Long supplyDetailId);

	List<CoreSupplyDetailEntity> queryDetailBatchCode(@Param("supplyMasterId") Long supplyMasterId, @Param("goodsName") String goodsName);

	Integer getSupplyNumberByPurchaseMasterId(@Param("purchaseMasterId") Long purchaseMasterId);

	Integer getSupplyNumberBySupplyMasterId(@Param("supplyMasterId") Long supplyMasterId);

	Integer getDetailLeaveAcceptQty(@Param("supplyDetailId") Long supplyDetailId);

	String getHospitalGoodsSpecsCode(@Param("goodsclass") Integer goodsclass, @Param("goodsid") Long goodsid, @Param("goodstypeid") Long goodstypeid, @Param("horgId") Long horgId, @Param("supplierId") Long supplierId);

	String getSupplierGoodsSpecsCode(@Param("goodsclass") Integer goodsclass, @Param("goodsid") Long goodsid, @Param("goodstypeid") Long goodstypeid);

	/**
	 * 获取供货细单
	 *
	 * @param supplyno  供货单编号
	 * @param goodsname 供应商商品名称
	 * @param goodsType 商品规格
	 * @return
	 * @Param lotno    生成批号
	 */
	List<CoreSupplyDetailEntity> getList(@Param("supplyno") String supplyno, @Param("goodsname") String goodsname, @Param("goodsType") String goodsType, @Param("lotno") String lotno);

    String selectConvert(@Param("goodsClass") Integer goodsClass, @Param("goodsId") Long goodsId);
}
