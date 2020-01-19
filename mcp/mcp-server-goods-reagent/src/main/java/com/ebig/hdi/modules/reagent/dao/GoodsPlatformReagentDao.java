package com.ebig.hdi.modules.reagent.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.reagent.entity.GoodsPlatformReagentEntity;
import com.ebig.hdi.modules.reagent.vo.GoodsPlatformReagentVO;

/**
 * 平台试剂信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-10 09:29:52
 */
public interface GoodsPlatformReagentDao extends BaseMapper<GoodsPlatformReagentEntity> {
	
	List<GoodsPlatformReagentVO> selectPlatformReagentList(Pagination page, Map<String, Object> params);
	
	GoodsPlatformReagentVO selectPlatformReagentById(@Param("id") Long id);

	List<Map<String, Object>> selectMatch(Map<String, Object> hospitalReagent);
}	
