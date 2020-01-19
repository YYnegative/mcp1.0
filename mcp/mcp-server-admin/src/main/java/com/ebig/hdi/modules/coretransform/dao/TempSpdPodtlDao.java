package com.ebig.hdi.modules.coretransform.dao;

import com.ebig.hdi.modules.coretransform.entity.TempSpdPodtlEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * temp_spd_podtl
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 21:24:46
 */
public interface TempSpdPodtlDao extends BaseMapper<TempSpdPodtlEntity> {
	
	//List<TempSpdPodtlEntity> getTempSpdPodtlEntity();
	List<TempSpdPodtlEntity> getTempSpdPodtlEntity(@Param("limit")Integer limit);
	
	Map<String, Object> getConsumablesGoods(@Param("sourcesId")String sourcesId);
	
	Map<String, Object> getDrugsGoods(@Param("sourcesId")String sourcesId);
	
	Map<String, Object> getReagentGoods(@Param("sourcesId")String sourcesId);
	
	Map<String, Object> getGoodsType(@Param("mgoodsid")String mgoodsid,@Param("uorganid")String uorganid);
	
	Map<String, Object> getIdAndName(@Param("supplyid") String supplyid,@Param("uorganid") String uorganid);
	
	Map<String, Object> getHospitalSupplierShip(@Param("sourcesHospitalId") String sourcesHospitalId, @Param("sourcesSupplierId") String sourcesSupplierId);

	Map<String, Object> getHospitalGoodsSpecs(@Param("hospitalId") String hospitalId, @Param("hospitalSourcesSpecsId") String hospitalSourcesSpecsId);

	Map<String, Object> getStorehouse(@Param("uorganid") String uorganid, @Param("orgdataid") String orgdataid);

	Map<String, Object> getSupplyMasterIdAndSupplyDetailId(@Param("podtlid") String podtlid);

}
