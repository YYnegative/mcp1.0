package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CoreLotEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:14
 */
public interface CoreLotDao extends BaseMapper<CoreLotEntity> {
	//根据deptId获取到供应商id
	CoreLotEntity getSupplierId(@Param("deptId") Long deptId);
	
	List<CoreLotEntity> selectByDeptId(Pagination page, Map<String, Object> params);
	
	List<CoreLotEntity> selectByBedingungen(Pagination page, Map<String, Object> params);
	
	void updateLotstatus(@Param("lotid") Long lotid,@Param("lotstatus") Integer lotstatus);

	void saveLot(CoreLotEntity entity);

	List<CoreLotEntity> getLotInfoByLotno(CoreLotEntity entity);

	//HDI转换用 根据lotno查询是否存在此批号
	List<CoreLotEntity> selectByLotno(@Param("lotno") String lotno);
	
	//根据原医院机构标识找到deptId
	Map<String, Object> getDeptId(@Param("uorganid") String uorganid,@Param("horganid") String horganid);

	CoreLotEntity selectCoreLot(@Param("goodsType") Integer goodsType, @Param("hospitalGoodsId") Long hospitalGoodsId, @Param("hospitalGoodsSpecsId") Long hospitalGoodsSpecsId, @Param("lotno") String lotno);
}
