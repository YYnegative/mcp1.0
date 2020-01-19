package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import com.ebig.hdi.modules.core.param.CorePurchaseParam;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CorePurchaseMasterEntity;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-27 11:12:34
 */
@Component
public interface CorePurchaseMasterDao extends BaseMapper<CorePurchaseMasterEntity> {
	
	//根据deptId获取到供应商id
	CorePurchaseMasterEntity getSupplierId(@Param("deptId") Long deptId);
	
	List<CorePurchaseMasterEntity> selectByDeptId(Pagination page, Map<String, Object> params); 
	
	List<CorePurchaseMasterEntity> selectByBedingungen(Pagination page, Map<String, Object> params); 
		
	void insertDetail(CorePurchaseMasterEntity entity);

	List<CorePurchaseMasterEntity> selectByPurchaseMasterId(@Param("purchaseMasterId") Long purchaseMasterId);
	
	CorePurchaseMasterEntity selectByMasterId(@Param("purchaseMasterId") Long purchaseMasterId);
	
	CorePurchaseMasterEntity selectMasterId(@Param("purchaseMasterId") Long purchaseMasterId);
	
	CorePurchaseMasterEntity selectPurchasestatus(@Param("purchaseMasterId") Long purchaseMasterId);
	
    //HDI转换用   查询是否存在此原始数据标识对应的主单
    CorePurchaseMasterEntity selectByOrgdataid(@Param("orgdataid") String orgdataid);

	CorePurchaseMasterEntity selectByPurplanno(@Param("purplanno") String purplanno);
	
	List<CorePurchaseMasterEntity> selectLikePurplanno(@Param("purplanno") String purplanno, @Param("supplierId") Long supplierId);

	/**
	 * 查询指定列的数据集合
	 * @param columns
	 * @param queryParam
     * @return List<CorePurchaseMasterEntity>
     */
	List<Map<String,Object>> getList(@Param("columns")String[] columns,@Param("queryParam") CorePurchaseParam queryParam);
}
