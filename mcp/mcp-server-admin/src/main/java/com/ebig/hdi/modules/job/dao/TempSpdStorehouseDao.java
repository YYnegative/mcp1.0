package com.ebig.hdi.modules.job.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ebig.hdi.modules.coretransform.entity.TempSpdStorehouseEntity;

/**
 * 临时库房表
 * @author yjq
 * @email 312962470@qq.com
 * @Date 2019-06-21 03:24:44
 */
public interface TempSpdStorehouseDao extends BaseMapper<TempSpdStorehouseEntity> {

	/**
	 * 查询上传的库房列表
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectStorehouseList(Map<String, Object> params);
	
}
