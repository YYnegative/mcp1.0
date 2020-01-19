package com.ebig.hdi.modules.coretransform.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.SpdPurplandtlEntity;

/**
 * 
 * 
 * @author yjq
 * @email 312962470@qq.com
 * @date 2019-08-19 17:46:37
 */
public interface SpdPurplandtlDao extends BaseMapper<SpdPurplandtlEntity> {

	Map<String, Object> selectHospitalGoodsBySourcesSpecsId(@Param("sourcesSpecsId") String sourcesSpecsId);

	/**
	 * 获取1000条数据
	 * @param row
	 * @return
     */
	List<SpdPurplandtlEntity> queryList(@Param("row") int row);
}
