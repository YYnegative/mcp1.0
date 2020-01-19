package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CoreAcceptDetailEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
public interface CoreAcceptDetailDao extends BaseMapper<CoreAcceptDetailEntity> {
	
	List<CoreAcceptDetailEntity> selectByAcceptMasterId(Pagination page, Map<String, Object> params);
	
	List<CoreAcceptDetailEntity> selectAcceptDetail(@Param("goodsclass") Integer goodsclass,@Param("goodstypeid") Long goodstypeid,@Param("goodsid") Long goodsid,@Param("hospitalId")Long hospitalId,@Param("supplierId")Long supplierId);
	
	List<CoreAcceptDetailEntity> selectAcceptDetails(@Param("goodsclass") Integer goodsclass,@Param("goodstypeid") Long goodstypeid,@Param("goodsid") Long goodsid,@Param("hospitalId")Long hospitalId,@Param("supplierId")Long supplierId);

	CoreAcceptDetailEntity selectByOrgdataidAndOrgdatadtlid(@Param("poid") String poid, @Param("podtlid") String podtlid);

	CoreAcceptDetailEntity selectHgoodsInfo(@Param("goodsclass") Integer goodsclass,@Param("goodstypeid") Long goodstypeid,@Param("goodsid") Long goodsid,@Param("hospitalId")Long hospitalId,@Param("supplierId")Long supplierId);

	Integer getAcceptNumberBySupplyMasterId(@Param("supplyMasterId") Long supplyMasterId);
	
}
