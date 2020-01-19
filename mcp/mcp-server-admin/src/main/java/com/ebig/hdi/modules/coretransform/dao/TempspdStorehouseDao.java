package com.ebig.hdi.modules.coretransform.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.TempSpdStorehouseEntity;

/**
 * spd_storehouse
 * 
 * @author frink
 * @email 
 * @date 2019-06-21 09:48:50
 */

public interface TempspdStorehouseDao extends BaseMapper<TempSpdStorehouseEntity> {
	
	TempSpdStorehouseEntity selectByShaddress(@Param("shaddressid") String shaddressid,@Param("horganid") String horganid);
	
	Map<String, Object> selectShaddress(@Param("shaddressid") String shaddressid,@Param("horganid") String horganid);
}
