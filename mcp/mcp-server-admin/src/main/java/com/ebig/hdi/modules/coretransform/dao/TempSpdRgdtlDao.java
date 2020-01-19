package com.ebig.hdi.modules.coretransform.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.TempSpdRgdtlEntity;
import org.springframework.stereotype.Component;

/**
 * spd_rgdtl
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 09:40:10
 */
@Component
public interface TempSpdRgdtlDao extends BaseMapper<TempSpdRgdtlEntity> {
	
	TempSpdRgdtlEntity selectGoods(@Param("goodsid")Long goodsid,@Param("goodstypeid")Long goodstypeid,@Param("goodstype")String goodstype,@Param("goodsclass")Integer goodsclass, @Param("horgId")Long horgId);
	
	Map<String, Object> selectApprovals(@Param("mgoodsid")String mgoodsid,@Param("goodstype")String goodstype);
	
	Map<String, Object> selectDrugsApprovals(@Param("goodsid")Long goodsid);
	
	Map<String, Object> selectReagentApprovals(@Param("goodsid")Long goodsid);
	
	Map<String, Object> selectConsumablesApprovals(@Param("goodsid")Long goodsid);
	
	List<Map<String, Object>> getLabelEntity(@Param("supplyDetailId")Long supplyDetailId,@Param("labelid")Long labelid);
	
	Map<String, Object> getSpdRgdtl(@Param("labelid")Long labelid);
	
	Map<String, Object> getSpdRg(@Param("labelid")Long labelid);

	void save(@Param("rgdtlid")String rgdtlid,@Param("rgid")String rgid,@Param("uorganid")String uorganid,@Param("sgoodsid")String sgoodsid,
			@Param("hgoodsid")String hgoodsid,@Param("sourceid")String sourceid,@Param("sourcedtlid")String sourcedtlid,
			@Param("originid")String originid,@Param("originno")String originno,@Param("uorganno")String uorganno,
			@Param("uorganname")String uorganname,@Param("supplyid")String supplyid,@Param("supplyno")String supplyno,
			@Param("supplyname")String supplyname,@Param("labelno")String labelno,@Param("labeltype")BigDecimal labeltype,
			@Param("sgoodsno")String sgoodsno,@Param("sgoodsname")String sgoodsname,@Param("sgoodstype")String sgoodstype,
			@Param("sgoodsunit")String sgoodsunit,@Param("hgoodsno")String hgoodsno,@Param("hgoodsname")String hgoodsname,
			@Param("hgoodstype")String hgoodstype,@Param("hgoodsunit")String hgoodsunit,@Param("approvedocno")String approvedocno,
			@Param("factorydoc")String factorydoc,@Param("factoryname")String factoryname,@Param("prodarea")String prodarea,
			@Param("sunitprice")BigDecimal sunitprice,@Param("sgoodsqty")BigDecimal sgoodsqty,@Param("hunitprice")BigDecimal hunitprice,
			@Param("hgoodsqty")BigDecimal hgoodsqty,@Param("plotno")String plotno,@Param("pproddate")Date pproddate,
			@Param("pinvaliddate")Date pinvaliddate,@Param("slotid")String slotid,@Param("slotno")String slotno,
			@Param("sproddate")Date sproddate,@Param("sinvaliddate")Date sinvaliddate,@Param("credate")Date credate,
			@Param("purplandocid")String purplandocid,@Param("purplandtlid")String purplandtlid,@Param("plotid")String plotid,
			@Param("unitid")String unitid,@Param("unitcode")String unitcode,@Param("batchid")String batchid);
}
