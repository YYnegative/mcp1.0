package com.ebig.hdi.modules.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.job.entity.MatchTaskEntity;

public interface MatchTaskDao extends BaseMapper<MatchTaskEntity>  {
	
	List<MatchTaskEntity> selectConsumables();
	
	List<MatchTaskEntity> selectDrugs();
		
	List<MatchTaskEntity> selectReagent();
	
	List<MatchTaskEntity> selectSupplierConsumables();
	
	List<MatchTaskEntity> selectSupplierDrugs();
	
	MatchTaskEntity selectSupplierDrugsApprovals(@Param("goodsId") Long goodsId,@Param("goodsApprovals") String goodsApprovals);
	
	List<MatchTaskEntity> selectSupplierReagent();
	
	//供应商匹对定时任务  查询ref表有无匹对数据
	List<Map<String, Object>> getRef(@Param("supplierId") Long supplierId,@Param("hospitalId") Long hospitalId);

	List<Map<String, Object>> matchingPlatformDrugs(Map<String, Object> params);
}
