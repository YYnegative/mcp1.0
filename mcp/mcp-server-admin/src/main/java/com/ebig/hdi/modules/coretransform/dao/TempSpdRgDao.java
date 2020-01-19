package com.ebig.hdi.modules.coretransform.dao;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.TempSpdRgEntity;
import org.springframework.stereotype.Component;

/**
 * spd_rg
 * 
 * @author frink
 * @email 
 * @date 2019-06-24 09:41:32
 */
@Component
public interface TempSpdRgDao extends BaseMapper<TempSpdRgEntity> {
	
	void save(@Param("rgid")String rgid,@Param("uorganid")String uorganid,@Param("rgno")String rgno,@Param("rgstatus")BigDecimal rgstatus,@Param("rgtype")BigDecimal rgtype,@Param("uorganno")String uorganno,@Param("uorganname")String uorganname,@Param("supplyid")String supplyid,@Param("supplyno")String supplyno,@Param("supplyname")String supplyname,@Param("sourceid")String sourceid,@Param("originid")String originid,@Param("originno")String originno,@Param("shaddressid")String shaddressid,@Param("credate")Date credate);
	
}
