package com.ebig.hdi.modules.core.dao;

import java.util.List;
import java.util.Map;

/**
 * 公共Dao层，增删改操作
 * @author zack
 */
public interface CommonDao {

	/**
	 * 插入
	 * @param entity
	 * @return
	 */
	Boolean insertEntity(Map<String, Object> params);

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
	Integer updateEntity(Map<String, Object> params);
	
	/**
	 * 删除
	 * @param params
	 * @return
	 */
	Integer deleteEntity(Map<String, Object> params);
	
	/**
	 * 通过ID查询
	 * @param params
	 * @return
	 */
	<B> B findById(Map<String, Object> params);
	
	/**
	 * 查询全部
	 * @param params
	 * @return
	 */
	<B> List<B> findAll(Map<String, Object> params);
}
