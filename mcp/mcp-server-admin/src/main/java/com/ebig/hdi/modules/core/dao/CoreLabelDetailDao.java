package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ebig.hdi.modules.core.entity.CoreLabelDetailEntity;

/**
 * 
 * 
 * @author frink
 * @email 
 * @date 2019-05-25 12:01:15
 */
public interface CoreLabelDetailDao extends BaseMapper<CoreLabelDetailEntity> {
	
	List<CoreLabelDetailEntity> selectByLabelId(Pagination page, Map<String, Object> params);
	
}
