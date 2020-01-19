package com.ebig.hdi.modules.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.ebig.hdi.common.utils.PageUtils;
import com.ebig.hdi.modules.core.entity.CoreSupplyDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
public interface CoreSupplyDetailService extends IService<CoreSupplyDetailEntity> {

	PageUtils queryPageDetail(Map<String, Object> params);
	
	List<Map<String, Object>> selectLot(Long goodsid,Integer goodsclass,Long goodstypeid,String lotno);
	
	List<CoreSupplyDetailEntity> queryDetailBatchCode(CoreSupplyDetailEntity coreSupplyDetail);
	
	//提交医院
	List<CoreSupplyDetailEntity> selectBySupplyMasterId(Long supplyMasterId);
	
	//HDI转换用  生成转换的供货细单
	void saveSupplyDetail(CoreSupplyDetailEntity entity);

	Integer getSupplyNumberByPurchaseMasterId(Long purchaseMasterId);

	Integer getSupplyNumberBySupplyMasterId(Long supplyMasterId);
	
	Integer getDetailLeaveAcceptQty(Long supplyDetailId);
	
	String getHospitalGoodsSpecsCode(Integer goodsclass, Long goodsid, Long goodstypeid, Long horgId,Long supplierId);

	String getSupplierGoodsSpecsCode(Integer goodsclass, Long goodsid, Long goodstypeid);

	/**
	 * 获取供货细单
	 * @param supplyno 供货单编号
	 * @param goodsname 供应商商品名称
	 * @param goodsType 商品规格
	 * @param lotno 生成批号
     * @return
     */
	List<CoreSupplyDetailEntity> getList(String supplyno,String goodsname,String goodsType,String lotno);


    String selectConvert(Integer goodsClass, Long goodsId);
}

