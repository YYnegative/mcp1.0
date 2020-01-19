package com.ebig.hdi.modules.job.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ebig.hdi.modules.job.entity.HdiEtlEntity;

/**
 * @author zack
 */
public interface HdiEtlDao {

	/**
	 * 查询所有配置
	 * @return
	 */
	List<HdiEtlEntity> findEtlConfig(int udflag);
	
	void updateTime(@Param("beginTime")Long beginTime, @Param("id")Long id);
	
	boolean lockConfigById(Long id);
}
