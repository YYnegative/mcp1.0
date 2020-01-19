package com.ebig.hdi.modules.drugs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.drugs.entity.GoodsPlatformDrugsEntity;
import com.ebig.hdi.modules.drugs.vo.GoodsPlatformDrugsEntityVo;

/**
 * 平台药品信息
 * 
 * @author luorx
 * @email 492699405@qq.com
 * @date 2019-05-08 14:21:25
 */
public interface GoodsPlatformDrugsDao extends BaseMapper<GoodsPlatformDrugsEntity> {

	GoodsPlatformDrugsEntity selectByDrugsName(@Param("drugsName") String drugsName);

	GoodsPlatformDrugsEntityVo selectPlatformDrugsById(@Param("id") Long id);

	List<GoodsPlatformDrugsEntityVo> selectPlatformDrugsList(Pagination page, Map<String, Object> params);

	List<Map<String, Object>> selectMatch(Map<String, Object> hospitalDrugs);
	
}
