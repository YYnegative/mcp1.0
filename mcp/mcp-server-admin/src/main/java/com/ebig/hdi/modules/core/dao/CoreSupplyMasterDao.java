package com.ebig.hdi.modules.core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CoreSupplyMasterEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-26 18:26:03
 */
public interface CoreSupplyMasterDao extends BaseMapper<CoreSupplyMasterEntity> {
	
	//根据deptId,医院名称，库房名称获取到供应商id name（平台及原系统）-医院id name （平台及原系统）--改造后
	CoreSupplyMasterEntity getSupplierIdNew(@Param("deptId") Long deptId,@Param("storehouseid") Long storehouseid,@Param("id") Long id);
	//根据deptId获取到供应商id
	CoreSupplyMasterEntity getSupplierId(@Param("deptId") Long deptId);

	List<CoreSupplyMasterEntity> selectByDeptId(Pagination page, Map<String, Object> params);
	
	List<CoreSupplyMasterEntity> selectByBedingungen(Pagination page, Map<String, Object> params);
	
	CoreSupplyMasterEntity selectBySupplyMasterId(@Param("supplyMasterId") Long supplyMasterId);
	
	CoreSupplyMasterEntity selectByMasterId(@Param("supplyMasterId") Long supplyMasterId);
	
	//提交医院时查询原医院系统Id
	Map<String, Object> selectByHorgIdAndSupplierId(@Param("horgId") Long horgId,@Param("supplierId") Long supplierId);
	
	//提交医院时查询原库房id
	Map<String, Object> getOrgdataid(@Param("storehouseid") Long storehouseid);
	
	//根据原数据标识 查询是否存在此主单
	CoreSupplyMasterEntity selectByOrgdataid(@Param("orgdataid")String orgdataid);
	
	List<Map<String, Object>> selectByGoodsId(@Param("goodsid") Long goodsid,@Param("goodsclass") Integer goodsclass,@Param("goodstypeid") Long goodstypeid);

	Integer selectLabelQtyByMasterId(@Param("supplyMasterId") Long supplyMasterId);

	Integer selectSupplyQtyByMasterId(@Param("supplyMasterId") Long supplyMasterId);

    List<Map<String, Object>> getList(HashMap<String, Object> map);
}
